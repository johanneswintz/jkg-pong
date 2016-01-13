Java-Server
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
        int fensterX = 1500; // Fensterbreite (xWert)
        int fensterY = 800; // Fensterhoehe (yWert)
        int pongLaengeL = 100;
        int pongLaengeR = 100;
        int pongVerkleinerung = (int) (1 * pongLaengeL) / (2 * scoreMax);
        int ballDicke = 15;
        int pongDicke = 15;
        int pongSeitenabstand = 35;
        int posx = (int) (fensterX / 2) - (ballDicke / 2);
        int posy = (int) (fensterY / 2) - (ballDicke / 2);
        double lpos = (fensterY / 2) - (pongLaengeL / 2);
        int lposAus = (int) (fensterY / 2) - (pongLaengeL / 2);
        double rpos = (fensterY / 2) - (pongLaengeR / 2);
        int rposAus = (int) (fensterY / 2) - (pongLaengeR / 2);
        long startTime = System.currentTimeMillis();
        long tempTime = System.currentTimeMillis() + 3000;
        double speedVerhaeltnisL = 0.6; /* ! <1.0 ! */// Verhaeltnis Ball-Pong also
                                                                                                        // [Ball*speedVerhaeltnis =
                                                                                                        // pongSpeed]
        double speedVerhaeltnisR = 0.6; /* ! <1.0 ! */// Verhaeltnis Ball-Pong also
                                                                                                        // [Ball*speedVerhaeltnis =
                                                                                                        // pongSpeed]
        byte speed = 10;
        
        
        byte playerCount = 0; //max 2
        byte ready = 0; //max 2
        
//------KONSTRUKTOR

        public Pong_Server() {
                super(42000);
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
                        input();
                        wandBerechnung();
                        ballBewegung();
                        richtungAendern();
                        paint();
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
        public void input() {/*
                if (Display.isCloseRequested()) {
                        Display.destroy();
                        Keyboard.destroy();
                        System.exit(0);
                }

                if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                        Display.destroy();
                        Keyboard.destroy();
                        System.exit(0);
                }
                while (Keyboard.next()) {
                        if (Keyboard.getEventKeyState()) {
                                if (Keyboard.getEventKey() == Keyboard.KEY_P) {
                                        pause = true;
                                        pause();
                                }
                        }
                }

                if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                        lpos += 1 * speed * speedVerhaeltnisL;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                        lpos -= 1 * speed * speedVerhaeltnisL;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                        rpos += 1 * speed * speedVerhaeltnisR;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                        rpos -= 1 * speed * speedVerhaeltnisR;
                }
                lposAus = (int) lpos;
                rposAus = (int) rpos;
        */}

//////WAND BERECHNUNG
        public void wandBerechnung() {
                if (lposAus <= -1) {
                        lpos = 0;
                        lposAus = 0;
                } else if (lposAus >= ((fensterY + 1) - pongLaengeL + (scoreL * pongVerkleinerung))) {
                        lpos = (fensterY - pongLaengeL + (scoreL * pongVerkleinerung));
                        lposAus = (int) lpos;
                }
                if (rposAus <= -1) {
                        rpos = 0;
                        rposAus = 0;
                } else if (rposAus >= ((fensterY + 1) - pongLaengeR + (scoreR * pongVerkleinerung))) {
                        rpos = (fensterY - pongLaengeR + (scoreR * pongVerkleinerung));
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
                                && ((posy - lposAus) < (pongLaengeL - (scoreL * pongVerkleinerung)) && (posy - lposAus) > -ballDicke)) {
                        richtungh = 0;
                } else if (posx <= (pongSeitenabstand + pongDicke)
                                && !((posy - lposAus) < (pongLaengeL - (scoreL * pongVerkleinerung)) && (posy - lposAus) > -ballDicke)) {
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
                                && ((posy - rposAus) < (pongLaengeR - (scoreR * pongVerkleinerung)) && (posy - rposAus) > -ballDicke)) {
                        richtungh = 1;
                } else if (posx >= (fensterX - (pongSeitenabstand + pongDicke + ballDicke))
                                && !((posy - rposAus) < (pongLaengeR - (scoreR * pongVerkleinerung)) && (posy - rposAus) > -ballDicke)) {
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
                }
                send(pClientIP, pClientPort,"FIELDSIZE:"+fensterX+":"+fensterY);
                System.out.println(pClientIP + " hat sich als " + playerCount+ "er verbunden!");
        }
        
        public void processMessage(String pClientIP, int pClientPort, String pMessage){
                if(pMessage.equals("READY")){
                        ready++;
                        System.out.println("Ready von: " + pClientIP);
                        if(ready == 2){
                                sendToAll("START");
                                System.out.println("Start gesendet");
                        }
                }
        }
        public void processClosedConnection(String pClientIP, int pClientPort){
                playerCount--;
                ready--;
                pause = true;
                System.out.println(pClientIP + " ist weg");
        }
}
