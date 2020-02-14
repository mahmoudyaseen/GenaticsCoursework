import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Knapsack {

    private int numOfItems , totalWeight ;
    private int[] weight , value;
    private ArrayList<String> individuals;
    private Random rand = new Random();

    public Knapsack(int numOfItems, int totalWeight, int[] weight, int[] value) {
        this.numOfItems = numOfItems;
        this.totalWeight = totalWeight;
        this.weight = weight;
        this.value = value;
        individuals = new ArrayList<>();//is this clean code?
    }

    private int fitnessFunction(String individual)
    {
        int individualValue = 0;
        for(int i = 0 ; i < numOfItems ; i++)
        {
            if(individual.charAt(i) == '1')
                individualValue += value[i];
        }
        return individualValue;
    }

    private int individualWeight(String individual)
    {
        int individualWeight = 0;
        for(int i = 0 ; i < numOfItems ; i++)
        {
            if(individual.charAt(i) == '1')
                individualWeight += weight[i];
        }
        return individualWeight;
    }

    //return if there is space to take this item
    private boolean canAddItemToIndividual(String individual , int itemIndex)
    {
        int remainderweight = totalWeight - individualWeight(individual);
        if(remainderweight >= weight[itemIndex])
            return true;
        else
            return false;
    }

    //generate random individuals without give any case higher probability
    private String generateRandomIndividual()
    {
        //put individual with zeros
        String individual = "";
        for(int i = 0 ; i < numOfItems ; i++)
            individual += "0";

        //put all possible places in remainderitems to make individuals more random
        ArrayList<Integer> remainderItems = new ArrayList<>();
        for(int i = 0 ; i < numOfItems ; i++)
            remainderItems.add(i);

        //set 1 or 0 randomly in any place randomly without give high probability to first items
        for(int i = 0 ; i < numOfItems ; i++) {
            int selectedIndex = rand.nextInt(remainderItems.size());
            int itemIndex = remainderItems.get(selectedIndex);
            remainderItems.remove(selectedIndex);
            if(rand.nextInt(2) == 1 && canAddItemToIndividual(individual , itemIndex))
                individual = individual.substring(0 , itemIndex) + '1' + individual.substring(itemIndex+1);
        }

        return individual;
    }

    //class of 2 strings
    private class individualPair
    {
        public String individual1 , individual2;
        public individualPair(String individual1, String individual2) {
            this.individual1 = individual1;
            this.individual2 = individual2;
        }
    }

    //randomly remove any item while invalid individual without give higher probability to any position in individual
    private String makeIndividualValid(String individual)
    {
        if(individualWeight(individual) <= totalWeight)
            return individual;

        //get index of items in individual
        ArrayList<Integer> indexSelected = new ArrayList<>();
        for(int i = 0 ; i < numOfItems ; i++ )
        {
            if( individual.charAt(i) == '1' )
                indexSelected.add(i);
        }

        //randomly remove any items while individual is not valid
        while (individualWeight(individual) > totalWeight)
        {
            int removeIndex = rand.nextInt(indexSelected.size());
            int itemIndex = indexSelected.get(removeIndex);
            indexSelected.remove(removeIndex);
            individual = individual.substring(0 , itemIndex) + '0' + individual.substring(itemIndex+1);
        }
        return individual;
    }

    //make crossover with handling the invalid individual without give higher probability to any position in individual
    private individualPair crossover(String individual1 , String individual2)
    {
        //get number between 1 and numberOfItems - 1
        int index = rand.nextInt(numOfItems - 1 ) + 1 ;
        //make crossover before this index
        String result1 = individual1.substring(0 , index) + individual2.substring(index);
        String result2 = individual2.substring(0 , index) + individual1.substring(index);
        result1 = makeIndividualValid(result1);
        result2 = makeIndividualValid(result2);
        return new individualPair(result1 , result2);
    }

    //mutation 9 bits every 100 bits
    private String mutation(String individual)
    {
        double probabilityOfMutation = 0.09;
        for(int i = 0 ; i < numOfItems ; i++)
        {
            if(Math.random() <= probabilityOfMutation)
            {
                if(individual.charAt(i) == '1')
                    individual = individual.substring(0 , i) + '0' + individual.substring( i + 1);
                else if(canAddItemToIndividual(individual , i))
                    individual = individual.substring(0 , i) + '1' + individual.substring( i + 1);
                    //can put it with 1 and call makeIndividualValid()
            }
        }
        return individual;
    }

    private class selectedPair
    {
        public int individualIndex1 , individualIndex2;
        public selectedPair(int individualIndex1, int individualIndex2) {
            this.individualIndex1 = individualIndex1;
            this.individualIndex2 = individualIndex2;
        }
    }

    //select randomly 2 individuals and return index of the max of them
    private int getIndexOfMaxOf2RandomIndividuals()//is this clean code or path individuals Arraylist parameter
    {
        int rand1 = rand.nextInt(individuals.size());
        int rand2 = rand.nextInt(individuals.size());
        if(fitnessFunction(individuals.get(rand1)) > fitnessFunction(individuals.get(rand2)))
            return rand1;
        else
            return rand2;
    }

    //select 2 max of 4 random individuals and return their index
    private selectedPair tournamentSelection()//is this clean code or path individuals Arraylist parameter
    {
        int index1 =  getIndexOfMaxOf2RandomIndividuals(), index2 = getIndexOfMaxOf2RandomIndividuals();
        while(index1 == index2)
        {
            index2 = getIndexOfMaxOf2RandomIndividuals();
        }
        return new selectedPair(index1 , index2);
    }

    //get max value brute force solu
    private int bruteForceSolu()
    {
        //...
        return 1000000;
    }

    //get the max individual in individuals arratlist
    private String getMaxIndividual(ArrayList<String>individuals)
    {
        int maxFitness = -1 ;
        String maxIndividual = "";
        for (int i = 0; i < individuals.size(); i++) {
            if (maxFitness < fitnessFunction(individuals.get(i))) {
                maxFitness = fitnessFunction(individuals.get(i));
                maxIndividual = individuals.get(i);
            }
        }
        return maxIndividual;
    }

    //get the min individual in individuals arratlist
    private String getMinIndividual(ArrayList<String>individuals)
    {
        int minFitness = 1000000 ;
        String minIndividual = "";
        for (int i = 0; i < individuals.size(); i++) {
            if (minFitness < fitnessFunction(individuals.get(i))) {
                minFitness = fitnessFunction(individuals.get(i));
                minIndividual = individuals.get(i);
            }
        }
        return minIndividual;
    }

    //process and return the best value with Elitism algorithm
    public String process(int numOfGenerations , int numOfIndividuals)
    {
        int optimalSolution = bruteForceSolu();

        individuals = new ArrayList<>();//clean code?

        //generate random individuals with size = numOfIndividuals
        for(int i = 0 ; i < numOfIndividuals ; i++)
            individuals.add(generateRandomIndividual());

        //loop while get the optimal solution or reach the max number of generation
        int maxFitness = -1 ;
        String maxIndividual = "";
        for(int j = 0 ; j < numOfGenerations && maxFitness < optimalSolution ; j++ ) {
            //get max individual
            maxIndividual = getMaxIndividual(individuals);
            maxFitness = fitnessFunction(maxIndividual);

            //generate n individuals by crossover of old individuals and put them in new arraylist
            ArrayList<String> newIndividuals = new ArrayList<>();
            //number of individuals must be even number
            for(int i = 0 ; i < individuals.size() / 2 ; i++) {
                //select 2 individuals
                selectedPair individualsSelected = tournamentSelection();
                String individual1 = individuals.get(individualsSelected.individualIndex1);
                String individual2 = individuals.get(individualsSelected.individualIndex2);
                //
                //System.out.print(individualsSelected.individualIndex1);
                //System.out.println(individualsSelected.individualIndex2);
                //
                //get new individual from crossover
                individualPair individualGenerated = crossover(individual1,individual2);
                String newIndividual1 = individualGenerated.individual1;
                String newIndividual2 = individualGenerated.individual2;
                newIndividuals.add(newIndividual1);
                newIndividuals.add(newIndividual2);
            }

            //apply mutation in new generation
            for(int i = 0 ; i < newIndividuals.size() ; i++)
            {
                String newIndividual = mutation(newIndividuals.get(i));
                newIndividuals.remove(i);
                newIndividuals.add(newIndividual);
            }

            //remove min individual from new generation and put the max one from old generation
            String minIndividual = getMinIndividual(newIndividuals);
            newIndividuals.remove(minIndividual);
            newIndividuals.add(maxIndividual);

            individuals = newIndividuals;

        }
        maxIndividual = getMaxIndividual(individuals);
        return maxIndividual;
    }

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        int numOfTestCases , numOfItems  , toltalWeight;
        //System.out.println("Please enter number of testcases: ");
        numOfTestCases = input.nextInt();
        for(int j = 0 ; j < numOfTestCases ; j++) {

            //System.out.println("Please enter number of items: ");
            numOfItems = input.nextInt();
            //System.out.println("Please enter total weight: ");
            toltalWeight = input.nextInt();

            int[] weight = new int[numOfItems], value = new int[numOfItems];

            for (int i = 0; i < numOfItems; i++) {
                //System.out.println("Please enter weight , value of item " + Integer.toString(i + 1) + ": ");
                weight[i] = input.nextInt();
                value[i] = input.nextInt();
            }

            Knapsack k = new Knapsack(numOfItems, toltalWeight, weight, value);
            String output = k.process(numOfItems * 400, numOfItems * 2);

            System.out.print("TestCase" + Integer.toString(j+1) + " : ");
            //System.out.println(output);
            //System.out.println("(Item Number , Item Weight , Item Value )");
            Integer individualValue = 0;
            for(Integer i = 0 ; i < numOfItems ; i++)
            {
                if(output.charAt(i) == '1')
                {
                    individualValue += value[i];
                    //System.out.println("("+ i.toString() + " , " + weight[i] + " , " + value[i] + " )");
                }
            }
            //System.out.println("the value = " + individualValue.toString());
            System.out.println(individualValue);
        }
    }
}
