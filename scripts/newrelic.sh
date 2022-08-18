docker run -d --restart unless-stopped \
  --name newrelic-gatling \
  -h $(hostname) \
  -e NR_ACCOUNT_ID=1747307 \
  -e NR_API_KEY="724c21f5196ada0de0bab03bc0225ba90863d273" \
  -e TAGS="gatling" \
  -p 8126:8126/udp \
  newrelic/nri-statsd:latest