workspace {
    model {
        trader = person "Trader" "Retail banking client"

        tradingSystem = softwareSystem "Trading Platform" {
            apiGateway = container "API Gateway" "Entry point for all client requests" "Spring Cloud Gateway"
            tradingGateway = container "Trading Gateway" "Routes and validates trading orders" "Spring Boot"
            orderService = container "Order Management Service" "Manages order lifecycle" "Spring Boot"
            portfolioService = container "Portfolio Service" "Manages client portfolios" "Spring Boot"
            marketDataService = container "Market Data Service" "Provides real-time market data" "Spring Boot"
            notificationEngine = container "Notification Engine" "Sends alerts and notifications" "Spring Boot"
            analyticsService = container "Trading Analytics Service" "Calculates trading analytics" "Spring Boot"
            entitlementService = container "Entitlement Service" "Provides access rights" "Spring Boot Security"{
                tags "security"
            }

            kafka = container "Kafka" "Async messaging between services" "Apache Kafka" {
                tags "messaging"
            }
            postgres = container "PostgreSQL" "Stores orders and portfolio data" "PostgreSQL" {
                tags "database"
            }
            redis = container "Redis" "Caches market data and sessions Ids" "Redis" {
                tags "cache"
            }
        }

        trader -> apiGateway "REST / WebSocket"
        apiGateway -> tradingGateway "Routes trading requests"
        apiGateway -> portfolioService "Fetches portfolio"
        apiGateway -> marketDataService "Fetches market data"
        apiGateway -> entitlementService "Creates session and requests rights"
        apiGateway -> redis "Caches sessions"


        entitlementService -> redis "Caches access rights (entitlements)"
        entitlementService -> postgres "Read/writes access roles/rights"
        tradingGateway -> kafka "Publishes OrderCreated"
        tradingGateway -> entitlementService "Checks TRADE, ORDER_READ rights"
        tradingGateway -> apiGateway "Takes sessionId from header"
        kafka -> orderService "Consumes OrderCreated"
        orderService -> postgres "Reads/writes orders"
        orderService -> kafka "Publishes OrderExecuted"
        kafka -> portfolioService "Consumes OrderExecuted"
        portfolioService -> postgres "Reads/writes portfolio"
        kafka -> notificationEngine "Consumes OrderExecuted"
        kafka -> analyticsService "Consumes OrderExecuted"
        marketDataService -> redis "Caches quotes"
        orderService -> entitlementService "Checks ORDER_MANAGE, ORDER_EXECUTE"
        portfolioService -> entitlementService "Checks PORTFOLIO_READ/WRITE"
        marketDataService -> entitlementService "Checks MARKET_READ"
        notificationEngine -> entitlementService "Optional: check rights"
    }

    views {
        systemContext tradingSystem "SystemContext" {
            include *
            autoLayout
        }

        container tradingSystem "Containers" {
            include *
            autoLayout
        }

        styles {
            element "Person" {
                shape Person
                background #08427B
                color #ffffff
            }
            element "messaging" {
                shape Pipe
                background #e8a020
                color #ffffff
            }
            element "database" {
                shape Cylinder
                background #336791
                color #ffffff
            }
            element "cache" {
                shape Cylinder
                background #c6302b
                color #ffffff
            }
            element "Container" {
                background #438DD5
                color #ffffff
            }
            element "security"{
                shape Cylinder
                background #32CD32
                color #ffffff
            }
        }
    }
}