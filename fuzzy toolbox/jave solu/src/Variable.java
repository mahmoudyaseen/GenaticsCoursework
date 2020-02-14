public class Variable {

    private String name;
    private Set[] sets;
    private int x ;

    public Variable(String name, Set[] sets, int x) {
        this.name = name;
        this.sets = sets;
        this.x = x;
    }

    public String getName() {
        return name;
    }

    public Set getSet(String setName)
    {
        for(int i = 0 ; i < sets.length ; i++)
            if(sets[i].getName().equals(setName))
                return sets[i];
        return null;
    }

    public double getMembership(String setName)
    {
        Set set = getSet(setName);
        if(set == null)
            throw new NullPointerException("wrong set name");
        return set.getMembership(x);
    }


}
