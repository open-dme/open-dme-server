package io.github.opendme.server.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "qr-code")
data class QrCodeConfig(
    var discoverUrl: String,
    var clientId: String,
    var clientSecret: String,
    var scope: String,
    var hostname: String
)
