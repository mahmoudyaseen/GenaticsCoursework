public class OutVariable {

    private Variable variable;

    public OutVariable(String name, Set[] sets) {
        this.variable = new Variable(name , sets , 0);
    }

    public double defuzzify(setNameAndMembership []memberships)
    {
        double centroidMembershipSum = 0;
        double membershipSum = 0;
        for(int i = 0 ; i < memberships.length ; i++)
        {
            double membership = memberships[i].membership;
            String setName = memberships[i].setName;
            Set set = variable.getSet(setName);
            centroidMembershipSum += set.getCentroid() * membership;
            membershipSum += membership;
        }
        double weightedAverageMean = centroidMembershipSum / membershipSum;
        return weightedAverageMean;
    }
}
