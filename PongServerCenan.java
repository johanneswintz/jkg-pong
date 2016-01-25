import abiturklassen.netzklassen.*;

public class Pong_Server extends Server{

    boolean pause = false;
    boolean tempPause = true;
    int richtungv = (Math.random() < 0.5) ? 0 : 1; // 0 = runter // 1 = hoch //
                                                    // 2+ = stop //
    int richtungh = (Math.random() < 0.5) ? 0 : 1; // 0 = rechts // 1 = links //
                                                    // 2+ = stop //
    byte scoreL = 0;
    byte scoreR = 0;
    byte scoreMax = 2; // Punkteanzahl, die zum siegen benoetigt wird
    int fensterX = 1280; // Fensterbreite (xWert)
    int fensterY = 720; // Fensterhoehe (yWert)
    int pongLaenge = 100;
    int ballDicke = 15;
    int pongDicke = 15;
    int pongSeitenabstand = 35;
    int posx = (int) (fensterX / 2) - (ballDicke / 2);
    int posy = (int) (fensterY / 2) - (ballDicke / 2);
    double lpos = (fensterY / 2) - (pongLaenge / 2);
    int lposAus = (int) lpos;
    double rpos = (fensterY / 2) - (pongLaenge / 2);
    int rposAus = (int) rpos;
    long startTime = System.currentTimeMillis();
    long tempTime = System.currentTimeMillis() + 3000;
    double speedVerhaeltnis = 0.6; /* ! <1.0 ! */// Verhaeltnis Ball-Pong also
                                                    // [Ball*speedVerhaeltnis =
                                                    // pongSpeed]

    byte speed = 10;
    
    
    byte playerCount = 0; //max 2
    String[] player = new String[2];
    int[] playerPort = new int[2];
    boolean[] ready = new boolean[2];
    
//------KONSTRUKTOR

    public Pong_Server() {
        super(42000);
        System.out.println("[DEBUG] - Server gestartet.");
        durchfuehrung();
    }

//////INIT
    public void init() {
        startTime = System.currentTimeMillis();
        tempTime = System.currentTimeMillis() + 3000;
        tempPause = true;
        pause = false;
        richtungv = (Math.random() < 0.5) ? 0 : 1; // 0 = runter // 1 = hoch //
                                                    // 2+ = stop //
        richtungh = (Math.random() < 0.5) ? 0 : 1; // 0 = rechts // 1 = links //
                                                    // 2+ = stop //
        scoreR = 0;
        scoreL = 0;
        posx = (int) (fensterX / 2) - (ballDicke / 2);
        posy = (int) (fensterY / 2) - (ballDicke / 2);
        durchfuehrung();
    }

//////DURCHFUEHRUNG
    public void durchfuehrung() {
        startTime = System.currentTimeMillis();
        tempTime = System.currentTimeMillis() + 3000;

        while (!pause) {
            startsequenz();
            wandBerechnung();
            ballBewegung();
            richtungAendern();
            paint();
            sendData();
        }
    }

//////STARTSEQUENZ
    public void startsequenz() {
        if (((startTime + 1000) <= System.currentTimeMillis())
                && ((startTime + 2000) >= System.currentTimeMillis())) {
            scoreR = 3;
            scoreL = 3;
            tempPause = true;
        }
        if (((startTime + 2000) <= System.currentTimeMillis())
                && ((startTime + 3000) >= System.currentTimeMillis())) {
            scoreR = 2;
            scoreL = 2;
            tempPause = true;
        }
        if (((startTime + 3000) <= System.currentTimeMillis())
                && ((startTime + 4000) >= System.currentTimeMillis())) {
            scoreR = 1;
            scoreL = 1;
            tempPause = true;
        }
        if (((startTime + 4000) <= System.currentTimeMillis())
                && ((startTime + 5000) >= System.currentTimeMillis())) {
            scoreR = 0;
            scoreL = 0;
            tempPause = false;
        }
        if ((tempTime + 1000) >= System.currentTimeMillis()) {
            tempPause = true;
        } else {
            tempPause = false;
        }
    }

//////INPUT
    public void input(String pClientIP, int direction) { //direction -> 0 up / 1 down

/*        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_P) {
                    pause = true;
                    pause();
                }
            }
        }*/
        if (player[0].equals(pClientIP) && direction==0) {
            lpos += 1 * speed * speedVerhaeltnis;
        }
        if (player[0].equals(pClientIP) && direction==1) {
            lpos -= 1 * speed * speedVerhaeltnis;
        }
        if (player[1].equals(pClientIP) && direction==0) {
            rpos += 1 * speed * speedVerhaeltnis;
        }
        if (player[1].equals(pClientIP) && direction==1) {
            rpos -= 1 * speed * speedVerhaeltnis;
        }
        lposAus = (int) lpos;
        rposAus = (int) rpos;
    }

//////WAND BERECHNUNG
    public void wandBerechnung() {
        if (lposAus <= -1) {
            lpos = 0;
            lposAus = 0;
        } else if (lposAus >= ((fensterY + 1) - pongLaenge)) {
            lpos = (fensterY - pongLaenge);
            lposAus = (int) lpos;
        }
        if (rposAus <= -1) {
            rpos = 0;
            rposAus = 0;
        } else if (rposAus >= ((fensterY + 1) - pongLaenge)) {
            rpos = (fensterY - pongLaenge);
            rposAus = (int) rpos;
        }
    }

//////BALL BEWEGUNG
    public void ballBewegung() {
        if (!tempPause) {
            if (richtungv == 0) {
                posy = posy + 1 * speed;
            } else if (richtungv == 1) {
                posy = posy - 1 * speed;
            }
            if (richtungh == 0) {
                posx = posx + 1 * speed;
            } else if (richtungh == 1) {
                posx = posx - 1 * speed;
            }
        }
    }

//////RICHTUNG
    public void richtungAendern() {
        if (posy >= (fensterY - ballDicke)) {
            richtungv = 1;
        } else if (posy <= 0) {
            richtungv = 0;
        } else if (posx <= (pongSeitenabstand + pongDicke)
                && ((posy - lposAus) < (pongLaenge) && (posy - lposAus) > -ballDicke)) {
            richtungh = 0;
        } else if (posx <= (pongSeitenabstand + pongDicke)
                && !((posy - lposAus) < (pongLaenge) && (posy - lposAus) > -ballDicke)) {
            richtungh = 0;
            scoreR++;
            tempPause = true;
            tempTime = System.currentTimeMillis();
            posx = (int) (fensterX / 2) - (ballDicke / 2);
            if ((scoreL >= scoreMax) || (scoreR >= scoreMax)) {
                paint();
                beenden();
            }
        } else if (posx >= (fensterX - (pongSeitenabstand + pongDicke + ballDicke))
                && ((posy - rposAus) < (pongLaenge) && (posy - rposAus) > -ballDicke)) {
            richtungh = 1;
        } else if (posx >= (fensterX - (pongSeitenabstand + pongDicke + ballDicke))
                && !((posy - rposAus) < (pongLaenge) && (posy - rposAus) > -ballDicke)) {
            richtungh = 1;
            scoreL++;
            tempPause = true;
            tempTime = System.currentTimeMillis();
            posx = (int) (fensterX / 2) - (ballDicke / 2);
            if ((scoreL >= scoreMax) || (scoreR >= scoreMax)) {
                paint();
                beenden();
            }
        }
    }

//////PAINT
    public void paint() {}

//////PAUSE
    public void pause() {
        richtungv += 2;
        richtungh += 2;
        while (pause) {

        }
        paint();
        
        richtungv -= 2;
        richtungh -= 2;
        pause = false;
        return;
    }
    
//////SEND DATA    
    public void sendData(){
    	if(playerCount==2){
    		send(player[0],playerPort[0],"DATA:"+posx+":"+posy+":"+lposAus+":"+rposAus+":"+scoreL+":"+scoreR);
    		send(player[1],playerPort[1],"DATA:"+posx+":"+posy+":"+rposAus+":"+lposAus+":"+scoreR+":"+scoreL);
    	}
    }
    
//////BEENDEN
    public void beenden() {
        if (scoreL > scoreR)
            System.err.println("Spieler 1 hat gewonnen!");
        if (scoreL < scoreR)
            System.err.println("Spieler 2 hat gewonnen!");
        if (scoreL == scoreR)
            System.err.println("Unentschieden!");
        pause = true;
        while (pause) {

        }
    }

//////MAIN
    public static void main(String[] args) {
        new Pong_Server();

    }
        
    public void processNewConnection(String pClientIP, int pClientPort){
        playerCount++;
        if(playerCount>2){
            closeConnection(pClientIP,pClientPort);
            playerCount--;
        }else{
            player[playerCount-1] = pClientIP + "";
            playerPort[playerCount-1] = pClientPort;
            sendInitData(pClientIP , pClientPort);
            System.out.println("[DEBUG] - " + player[playerCount-1] + " hat sich als " + playerCount+ "er verbunden!");
        }
    }
    
    public void processMessage(String pClientIP, int pClientPort, String pMessage){
        if(pMessage.equals("READY")){
            ready[(pClientIP==player[0])?0:1] = true;
            System.out.println("[DEBUG] - Ready von: " + pClientIP);
            if(ready[0]&&ready[1]){    
                sendToAll("START");        
                System.out.println("[DEBUG] - Start gesendet");
            }        
        }
        
        if(pMessage.split(":")[0].equals("SETPOS")){
        	input(pClientIP, Integer.parseInt(pMessage.split(":")[1]));
        }
    }
    
    public void processClosedConnection(String pClientIP, int pClientPort){
        playerCount--;
        ready[(pClientIP==player[0])?0:1] = false;
        player[(pClientIP==player[0])?0:1] = "";
        playerPort[pClientIP==player[0]?0:1] = 0;
        pause = true;
        System.out.println("[DEBUG] - " + pClientIP + " ist weg");
    }
    
    public void sendInitData(String pClientIP, int pClientPort){
        if(player[0].equals(pClientIP)){
        	send(pClientIP, pClientPort, "INIT:"+fensterX+":"+fensterY+":"+posx+":"+posy+":"+ballDicke+":"+pongSeitenabstand+":"+lposAus+":"+pongDicke+":"+pongLaenge+":"+(fensterX-(pongSeitenabstand+pongDicke))+":"+rposAus);
        }else{
        	send(pClientIP, pClientPort, "INIT:"+fensterX+":"+fensterY+":"+posx+":"+posy+":"+ballDicke+":"+(fensterX-(pongSeitenabstand+pongDicke))+":"+rposAus+":"+pongDicke+":"+pongLaenge+":"+pongSeitenabstand+":"+lposAus);
        }
    }
}
