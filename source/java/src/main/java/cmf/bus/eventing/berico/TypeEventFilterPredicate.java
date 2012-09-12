package cmf.bus.eventing.berico;

import cmf.bus.eventing.IEventFilterPredicate;

public class TypeEventFilterPredicate implements IEventFilterPredicate {

    protected Class<?> clazz;
    
    public TypeEventFilterPredicate(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean filter(Object event) {
        return event.getClass().equals(clazz);
    }
}
