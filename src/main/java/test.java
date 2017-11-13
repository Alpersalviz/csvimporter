
import java.util.Arrays;
import java.util.Random;

/**
 * Created by alper on 9.05.2017.
 */
public class test
{

    public static void main(String[] args) {

        int[] okeyStones = new int[53];

        int[] user1 = {};
        int[] user2 = {};
        int[] user3 = {};
        int[] user4 = {};

        int[][] users = {user1 , user2, user3, user4};

        Random rand = new Random();
        for (int i = 0; i < 53; i++) {
            okeyStones[i] = i;

            int random_integer = -1;

            while(exists(random_integer, okeyStones)) {
                random_integer = rand.nextInt(53);
            }
            okeyStones[i] = random_integer;
            }
            int okeyStone = okeyStones[rand.nextInt(52)];

        switch(okeyStone) {
            case 12 :
               okeyStone = 1;
                break;

            case 25 :
                okeyStone = 13;
                break;

            case 38 :
                okeyStone = 26;
                break;

            case 51 :
                okeyStone = 39;
                break;
            default :
                okeyStone = okeyStone + 1;
        }
        Random randx = new Random();
        int userRand = randx.nextInt(4);
        users[userRand] = new int[14];

        for (int x = 0; x < 4 ; x++ ){
            if(userRand != x)
                    users[x] = new int[13];

        }

        for(int i = 0; i < okeyStones.length; i++) {
            if (i < 14){
                users[userRand][i] = okeyStones[i];
            }



        }

        int[] newOkeyStones = Arrays.copyOfRange(okeyStones, 14,okeyStones.length);
        for (int y = 0; y < 4  ;y++) {

            if (userRand != y){
                 for(int i = 0; i < newOkeyStones.length ; i++) {
                     if (i >= (y * 13) && i < (y + 1) * 13){
                         users[y][i] = newOkeyStones[i];
                 }
            }
        }else if(userRand == y){
                for(int i = 0; i < newOkeyStones.length ; i++) {
                    if (i >= (y * 13) && i < (y + 1) * 13){
                        users[y + 1][i] = newOkeyStones[i];
                    }

            }
            }

        }





        /* Soruda  1 kişiye 15 geriye kalan 3 kişiye 14 taş dağılması denmiş ama 15+(14x3) = 57 yapıyor galiba 14+(13x3) = 53 yapıcaz*/



           // System.out.print("okey" + okeyStone );


        System.out.print(Arrays.toString(okeyStones) + "\n" );
        System.out.print(Arrays.toString(users[userRand] )+ "\n");
        System.out.print(Arrays.toString(newOkeyStones) + "\n" );



        System.out.print(Arrays.toString(users[0]));
        System.out.print(Arrays.toString(users[1]));
        System.out.print(Arrays.toString(users[2]));
        System.out.print(Arrays.toString(users[3]));

        }



    public static boolean exists(int number, int[] array) {
        if (number == -1)
            return true;

        for (int i=0; i<array.length; i++) {
            if (number == array[i])
                return true;
        }
        return false;
    }


}
