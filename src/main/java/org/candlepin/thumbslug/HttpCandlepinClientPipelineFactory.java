/**
 * Copyright (c) 2011 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package org.candlepin.thumbslug;

import static org.jboss.netty.channel.Channels.*;

import javax.net.ssl.SSLEngine;

import org.candlepin.thumbslug.ssl.SslContextFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.jboss.netty.handler.ssl.SslHandler;

/**
 * HttpCandlepinClientPipelineFactory - build a pipeline for thumbslug to talk to candlepin
 * (and request an entitlement certificate)
 */
class HttpCandlepinClientPipelineFactory {
    private Config config;
    
    public HttpCandlepinClientPipelineFactory(Config config) {
        this.config = config;

    }

    public ChannelPipeline getPipeline(Channel client, boolean useSSL,
        boolean keepAlive)
        throws Exception {
        ChannelPipeline pipeline = pipeline();
        
        if (useSSL) {
            SSLEngine engine = SslContextFactory.getCandlepinClientContext().createSSLEngine();
            engine.setUseClientMode(true);
            pipeline.addLast("ssl", new SslHandler(engine));
        }
        
        pipeline.addLast("codec", new HttpClientCodec());

        // we're explicitly not decompressing here, because we don't read the body,
        // only pass it on.
        // pipeline.addLast("inflater", new HttpContentDecompressor());

        // Uncomment the following line if you don't want to handle HttpChunks.
        // pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));

        pipeline.addLast("handler", new HttpRelayingResponseHandler(client, keepAlive));
        return pipeline;
    }

}