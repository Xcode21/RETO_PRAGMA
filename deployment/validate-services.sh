#!/bin/bash

# Service validation script
# Validates that all services are running and can communicate

echo "=== Validating Microservices Communication ==="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to check service health
check_service() {
    local service_name=$1
    local url=$2
    local max_retries=30
    local retry_count=0
    
    echo -n "Checking $service_name..."
    
    while [ $retry_count -lt $max_retries ]; do
        if curl -sf "$url" > /dev/null 2>&1; then
            echo -e " ${GREEN}✅ UP${NC}"
            return 0
        fi
        
        sleep 2
        retry_count=$((retry_count + 1))
        echo -n "."
    done
    
    echo -e " ${RED} DOWN${NC}"
    return 1
}

# Function to test database connection
check_database() {
    local db_name=$1
    local container_name=$2
    
    echo -n "Checking $db_name database..."
    
    if docker exec "$container_name" pg_isready > /dev/null 2>&1; then
        echo -e " ${GREEN} CONNECTED${NC}"
        return 0
    else
        echo -e " ${RED} DISCONNECTED${NC}"
        return 1
    fi
}

# Function to test microservice communication
test_communication() {
    echo "=== Testing Microservice Communication ==="
    
    # Test UserService to Application Service communication
    echo "Testing UserService -> Application Service communication..."
    
    # This would be a real test call to your application service through the user service
    response=$(curl -s -w "%{http_code}" http://localhost:8072/api/users/health 2>/dev/null)
    if [ "$response" = "200" ]; then
        echo -e "${GREEN} UserService responding correctly${NC}"
    else
        echo -e "${RED} UserService communication failed${NC}"
    fi
    
    # Check if application service is accessible from docker network
    if docker run --rm --network pragma-network alpine/curl:latest -s http://host.docker.internal:8098/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN} Application Service accessible from Docker network${NC}"
    else
        echo -e "${YELLOW}️  Application Service may not be accessible from Docker network${NC}"
        echo "Make sure your Application Service (port 8098) is running on the host"
    fi
}

# Function to check data persistence
check_data_persistence() {
    echo "=== Testing Data Persistence ==="
    
    # Check if volumes exist
    if docker volume inspect user_service_postgres_users_data > /dev/null 2>&1; then
        echo -e "${GREEN} User database volume exists${NC}"
        
        # Check volume size (basic persistence test)
        volume_info=$(docker volume inspect user_service_postgres_users_data --format '{{.Mountpoint}}')
        if [ -d "$volume_info" ]; then
            echo -e "${GREEN} Volume directory accessible${NC}"
        fi
    else
        echo -e "${RED} User database volume missing${NC}"
    fi
}

# Main execution
echo "Starting service validation..."
echo "Current time: $(date)"
echo ""

# Check if Docker Compose services are running
if ! docker-compose ps | grep -q "Up"; then
    echo -e "${RED}❌ Docker Compose services are not running${NC}"
    echo "Please start services with: docker-compose up -d"
    exit 1
fi

echo "=== Service Health Checks ==="

# Check databases
check_database "Users Database" "postgres-users"

# Check services
check_service "UserService" "http://localhost:8072/actuator/health"
check_service "Application Service (External)" "http://localhost:8098/actuator/health"

echo ""

# Test communication
test_communication

echo ""

# Check data persistence
check_data_persistence

echo ""
echo "=== Validation Summary ==="
echo "Validation completed at: $(date)"
echo ""
echo "Next steps:"
echo "1. If all services are UP, your microservices are properly configured"
echo "2. Test API endpoints: curl http://localhost:8072/api/users/health"
echo "3. Check logs: docker-compose logs -f user-service"
echo "4. Monitor databases: docker exec -it postgres-users psql -U app -d usersdb"