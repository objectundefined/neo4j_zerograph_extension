package org.zerograph;

/**
 * Created by gabriellipson on 3/7/14.
 */

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.Description;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.extension.KernelExtensionFactory;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.kernel.lifecycle.Lifecycle;


import static org.neo4j.helpers.Settings.HOSTNAME_PORT;
import static org.neo4j.helpers.Settings.INTEGER;
import static org.neo4j.helpers.Settings.setting;

import org.neo4j.graphdb.config.Setting;
import org.neo4j.helpers.HostnamePort;
import org.zerograph.ZerographServer;


import static org.neo4j.helpers.Settings.*;


public class ZerographKernelExtensionFactory extends KernelExtensionFactory<ZerographKernelExtensionFactory.Dependencies> {

    @Description("Settings for the Zerograph Server Extension")
    public static abstract class CypherRemotingSettings {
        public static Setting<HostnamePort> zerograph_address = setting( "zerograph_address", HOSTNAME_PORT, ":5555" );
        public static Setting<Integer> zerograph_threads = setting( "zerograph_threads", INTEGER, "10");
    }

    public ZerographKernelExtensionFactory() {
        super(ZerographServer.SERVICE_NAME);
    }

    @Override
    public Lifecycle newKernelExtension(Dependencies dependencies) throws Throwable {
        Config config = dependencies.getConfig();
        return new ZerographServer(dependencies.getGraphDatabaseService(),dependencies.getStringLogger(), config.get(CypherRemotingSettings.zerograph_address),config.get(CypherRemotingSettings.zerograph_threads));
    }

    public interface Dependencies {
        GraphDatabaseService getGraphDatabaseService();
        StringLogger getStringLogger();
        Config getConfig();
    }
}
