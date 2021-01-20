/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolrobot;


import com.leapmotion.leap.*;
import java.io.IOException;
/**
 *
 * @author Laptop
 */
    class LeapMotion extends Listener {

        //private GUI gui;

        @Override
        public void onInit(Controller controller) {
            System.out.println("Initialized");
        }

        @Override
        public void onConnect(Controller controller) {
            System.out.println("Connected");
            controller.enableGesture(Gesture.Type.TYPE_SWIPE);
            controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
            controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
            controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
        }

        @Override
        public void onDisconnect(Controller controller) {
            //Note: not dispatched when running in a debugger.
            System.out.println("Disconnected");
        }

        @Override
        public void onExit(Controller controller) {
            System.out.println("Exited");
        }

        @Override
        public void onFrame(Controller controller) {
            // Get the most recent frame and report some basic information
            Frame frame = controller.frame();
            /*        System.out.println("Frame id: " + frame.id()
             + ", timestamp: " + frame.timestamp()
             + ", hands: " + frame.hands().count()
             + ", fingers: " + frame.fingers().count()
             + ", tools: " + frame.tools().count()
             + ", gestures " + frame.gestures().count());
             */
            //Get hands
            
            for (Hand hand : frame.hands()) {
                String handType = hand.isLeft() ? "Left hand" : "Right hand";
//            System.out.println("  " + handType + ", id: " + hand.id()
//                             + ", palm position: " + hand.palmPosition());

                // Get the hand's normal vector and direction
                Vector normal = hand.palmNormal();
                Vector direction = hand.direction();

                // Mendapatkan Setiap Jari Pada Tangan
                float[] allDistance = new float[5];
                float Sum=0;
                float Rata=0;
                int k = 0;
                for (Finger finger : hand.fingers()) {
//                System.out.println("    " + finger.type() + ", id: " + finger.id()
//                                 + ", length: " + finger.length()
//                                 + "mm, width: " + finger.width() + "mm");

                    //Mendapatkan Nilai Vector Setiap Bone
                    float[] distance = new float[4];
                    int i = 0;
                    for (Bone.Type boneType : Bone.Type.values()) {
                        Bone bone = finger.bone(boneType);
//                    System.out.println("        " + bone.type()
//                                     + " bone, start: " + bone.prevJoint());
                        //+ ", end: " + bone.nextJoint()
                        // + ", direction: " + bone.direction())                
                        //}

                //Posisi Palm
//                System.out.println("        Palm position: " + hand.palmPosition());
                        //Menghitung Jarak Antara Posisi Palm dengan Posisi Bone
                        Vector aPoint = new Vector(hand.palmPosition());
                        Vector bPoint = new Vector(bone.prevJoint());
                        float jarak = bPoint.distanceTo(aPoint); // distance = 10
//                System.out.println("        Jarak dari Palm: " + jarak);               
                        distance[i] = jarak;
                        i++;
                        //------------------------------------------------------------------------------------------------------------------

                    }

                    allDistance[k] = distance[3];
                    System.out.print(allDistance[k]+"\t");
                    //Sum = Sum + allDistance[k];
                    //System.out.println("AD : "+allDistance[k]+" D : "+distance[3]);
                    k++;
                    
                    //System.out.println("i = "+i);
                    //System.out.println("k = "+k);
               
                    
                    //System.out.println("JML = "+Sum);
                }
                System.out.println();
                /*
                Rata = Sum / 5;
                System.out.println("Rata = "+Rata);
                if (Rata < 48){
                    System.out.println("A");
                }else if(Rata > 48 && Rata < 52){
                    System.out.println("B");
                }else if(Rata > 52 && Rata < 56){
                    System.out.println("C");
                }else if(Rata > 56 && Rata < 70){
                    System.out.println("D");
                }else if(Rata > 70){
                    System.out.println("STOP");
                }
                //System.out.println("k = "+k);
                System.out.print("\n\n-------------------------------------------------------------------------------------------------------------------------------\n");
                */
            }
            //System.out.print("\n \n \n");
        }
    }


class Sample {
    public static void main(String[] args) {
        // Create a sample listener and controller
        LeapMotion listener = new LeapMotion();
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);

        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
    }
}