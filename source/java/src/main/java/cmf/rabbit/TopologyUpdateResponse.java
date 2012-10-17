package cmf.rabbit;

public class TopologyUpdateResponse {

    private ITopologyRegistry topologyRegistry;

    public ITopologyRegistry getTopologyRegistry() {
        return topologyRegistry;
    }

    public void setTopologyRegistry(ITopologyRegistry topologyRegistry) {
        this.topologyRegistry = topologyRegistry;
    }
}
