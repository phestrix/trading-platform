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

            kafka = container "Kafka" "Async messaging between services" "Apache Kafka" {
                tags "messaging"
            }
            postgres = container "PostgreSQL" "Stores orders and portfolio data" "PostgreSQL" {
                tags "database"
            }
            redis = container "Redis" "Caches market data" "Redis" {
                tags "cache"
            }
        }

        trader -> apiGateway "REST / WebSocket"
        apiGateway -> tradingGateway "Routes trading requests"
        apiGateway -> portfolioService "Fetches portfolio"
        apiGateway -> marketDataService "Fetches market data"

        tradingGateway -> kafka "Publishes OrderCreated"
        kafka -> orderService "Consumes OrderCreated"
        orderService -> postgres "Reads/writes orders"
        orderService -> kafka "Publishes OrderExecuted"
        kafka -> portfolioService "Consumes OrderExecuted"
        portfolioService -> postgres "Reads/writes portfolio"
        kafka -> notificationEngine "Consumes OrderExecuted"
        kafka -> analyticsService "Consumes OrderExecuted"
        marketDataService -> redis "Caches quotes"
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
        }
    }
}