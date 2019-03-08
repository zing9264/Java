import java.util.*;

public class Simpletron {
    //Registers:
    private int accumulator; //用來保存即將計算的數字
    private int instructionCounter; //指向當前指令編號
    private int instructionRegister;// 暫存即將執行的指令
    private int operationCode; //指出當前的運算符
    private int operand;//指出當前要運算的變數
    private int[] memory; //100條指令的佔位符

    public Simpletron() {
        display();
        init();
    }

    public void display() {
        System.out.print(
                "*** Welcome to Simpletron! ***" + '\n' +
                        "*** Please enter your program one instruction ***" + '\n' +
                        "*** (or data word) at a time into the input   ***" + '\n' +
                        "*** text field. I will display the location   ***" + '\n' +
                        "*** number and a question mark (?). You then  ***" + '\n' +
                        "*** type the word for that location. Press the***" + '\n' +
                        "*** Done button to stop entering your program ***" + '\n' +
                        "Location" + "Instruction" + "    ****" + "*****" + '\n');
    }

    public void init() {
        memory = new int[100];
        instructionCounter = 0;
    }

    public void run() {
        int getInstruction = 0;
        int mPtr = 0; //ptr for memory
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print(mPtr + " ? ");
            getInstruction = input.nextInt();
            if(getInstruction == -99999)
                break;
            memory[mPtr] = getInstruction;
            mPtr++;
        }
        System.out.print("*** Program loading completed ***\n" +
                "*** Program excecution begins  ***\n");

        for (int data : memory) {
            if (data != 0) {
                load();
                execute(operationCode, operand);
            }
        }
    }

    public void load() {
        instructionRegister = memory[instructionCounter];
        operationCode = instructionRegister / 100;
        operand = instructionRegister % 100;
    }

    public void execute(int opC, int opR) {

        switch (opC) {
            case 10:  // read
                Scanner input = new Scanner(System.in);
                System.out.print("輸入變數:");
                memory[opR] = input.nextInt();
                if(memory[opR]>9999 || memory[opR]<-9999){
                    fatalErrors("overflow");
                }
                break;
            case 11: //write
                System.out.println("輸出: " + memory[opR]);
                break;
            case 20: //load
                accumulator = memory[opR];
                break;
            case 21: //store
                memory[opR]= accumulator;
                break;
            case 30: // add
                accumulator += memory[opR];
                if(accumulator>9999 || accumulator<-9999){
                    fatalErrors("overflow");
                }
                break;
            case 31: // sub
                accumulator -= memory[opR];
                if(accumulator>9999 || accumulator<-9999){
                    fatalErrors("overflow");
                }
                break;
            case 32: //div
                if(memory[opR]==0){
                    fatalErrors("dividebyZero");
                }
                accumulator /= memory[opR];
                if(accumulator>9999 || accumulator<-9999){
                    fatalErrors("overflow");
                }
                break;
            case 33: // mul
                accumulator *= memory[opR];
                if(accumulator>9999 || accumulator<-9999){
                    fatalErrors("overflow");
                }
                break;
            case 40:// branch
                instructionCounter= opR;
                break;
            case 41://branch neg
                if(accumulator<0){
                    instructionCounter=opR;
                }
                break;
            case 42://branch zero
                if(accumulator==0){
                    instructionCounter=opR;
                }
                break;
            case 43:// halt
                halt();
                System.out.print("*** Simpletron execution terminated ***");
                System.exit(0);
                break;
            default:
                fatalErrors("invalidopC");
        }
        instructionCounter++;
    }

    public void halt(){
        System.out.print("REGISTERS:\n"+
                "accumulator:          "+accumulator+'\n'+
                "instructionCounter:   "+instructionCounter+'\n'+
                "instructionRegister:  "+instructionRegister+'\n'+
                "operationCode:        "+operationCode+'\n'+
                "operand:              "+operand+'\n'+'\n'+
                "MEMORY:\n"
                );
        for( int i=0 ;i<10;i++){
            System.out.printf ( "%6d", i);
        }
        System.out.print ('\n');
        int cnt=0;

        for( int i=0 ;i<10;i++){
            if(cnt%10==0){
                System.out.printf("%2d ",cnt);
            }
            for (int j=0;j<10;j++){
                if(memory[cnt]==0){
                    System.out.print("+0000 ");
                }
                else {
                    System.out.printf ("+%4d ", memory[cnt]);
                }
                cnt++;
            }
            System.out.print ('\n');
        }
    }

    public void fatalErrors( String Ename){
        halt();
        switch (Ename){
            case "dividebyZero":
                System.out.println("*** Attempt to divide by zero ***");
                System.out.println("*** Simpletron execution abnormally terminated ***");
                System.exit(1);
                break;
            case "invalidopC":
                System.out.println("*** Attempt to execute invalid operation code ***");
                System.out.println("*** Simpletron execution abnormally terminated ***");
                System.exit(1);
                break;
            case "overflow":
                System.out.println("*** Memory overflows ***");
                System.out.println("*** Simpletron execution abnormally terminated ***");
                System.exit(1);
                break;
        }

    }
}
