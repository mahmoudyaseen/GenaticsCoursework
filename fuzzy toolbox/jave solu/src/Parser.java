import jdk.nashorn.internal.ir.SetSplitState;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

    private Variable getVariable(Variable[] variables , String variableName)
    {
        for(int i = 0 ; i  <  variables.length ; i++)
            if(variables[i].getName().equals(variableName))
                return variables[i];
        return null;
    }

    static public void main (String []args) throws FileNotFoundException {
        Parser parser = new Parser();
        File file = new File("input.txt");
        Scanner in = new Scanner(file);
        int numberOfVariables = in.nextInt();
        Variable[] variables = new Variable[numberOfVariables];
        //read variables
        for (int i = 0 ; i < numberOfVariables ; i++)
        {
            String variableName = in.next();
            int value = in.nextInt();
            int numberOfSets = in.nextInt();
            Set[] sets = new Set[numberOfSets];
            //read sets
            for(int j = 0 ; j < numberOfSets ; j++)
            {
                String setName = in.next();
                String type = in.next();
                if(type.equals("trapezoidal"))
                {
                    Point point1 = new Point(in.nextInt() , 0);
                    Point point2 = new Point(in.nextInt() , 1);
                    Point point3 = new Point(in.nextInt() , 1);
                    Point point4 = new Point(in.nextInt() , 0);
                    sets[j] = new Trapezoidal(setName , point1 , point2 , point3 , point4);
                }
                else if(type.equals("triangle"))
                {
                    Point point1 = new Point(in.nextInt() , 0);
                    Point point2 = new Point(in.nextInt() , 1);
                    Point point3 = new Point(in.nextInt() , 0);
                    sets[j] = new Triangle(setName, point1,point2,point3);
                }
                else
                    throw new Error("set type not supported");
            }
            variables[i] = new Variable(variableName , sets , value);
        }
        //read outvariable
        String variableName = in.next();
        int numberOfSets = in.nextInt();
        Set[] sets = new Set[numberOfSets];
        //read sets
        for(int j = 0 ; j < numberOfSets ; j++)
        {
            String setName = in.next();
            String type = in.next();
            if(type.equals("trapezoidal"))
            {
                Point point1 = new Point(in.nextInt() , 0);
                Point point2 = new Point(in.nextInt() , 1);
                Point point3 = new Point(in.nextInt() , 1);
                Point point4 = new Point(in.nextInt() , 0);
                sets[j] = new Trapezoidal(setName , point1 , point2 , point3 , point4);
            }
            else if(type.equals("triangle"))
            {
                Point point1 = new Point(in.nextInt() , 0);
                Point point2 = new Point(in.nextInt() , 1);
                Point point3 = new Point(in.nextInt() , 0);
                sets[j] = new Triangle(setName, point1,point2,point3);
            }
            else
                throw new Error("set type not supported");
        }
        OutVariable outvariable = new OutVariable(variableName , sets);
        //rules
        int numberOfRules = in.nextInt();
        setNameAndMembership[] memberships = new setNameAndMembership[numberOfRules];
        for(int i = 0 ; i < numberOfRules ; i++)
        {
            int numberOfPremises = in.nextInt();
            //read first premise
            variableName = in.next();
            in.next();//ignore '='
            String setName = in.next();
            double inference = parser.getVariable(variables , variableName).getMembership(setName);
            for(int j = 0 ; j < numberOfPremises-1 ; j++)
            {
                String operator = in.next();
                variableName = in.next();
                in.next();//ignore '='
                setName = in.next();
                if(operator.equals("AND"))
                    inference = Math.min(inference , parser.getVariable(variables , variableName).getMembership(setName));
                else if(operator.equals("OR"))
                    inference = Math.max(inference , parser.getVariable(variables , variableName).getMembership(setName));
                else
                    throw new Error("Operator Not Found");
            }
            in.next();//ignore then
            variableName = in.next();
            in.next();//ignore '='
            setName = in.next();
            memberships[i] = new setNameAndMembership(setName , inference);
        }
        System.out.println(outvariable.defuzzify(memberships));
    }
}
