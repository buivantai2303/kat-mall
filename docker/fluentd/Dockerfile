# ============================================================================
# KATMALL - FLUENTD DOCKERFILE
# Author: tai.buivan@outlook.com
# Description: Custom Fluentd image with required plugins
# ============================================================================

FROM fluent/fluentd:v1.16-debian

USER root

# Install plugins
RUN gem install fluent-plugin-elasticsearch \
    && gem install fluent-plugin-rewrite-tag-filter \
    && gem install oj

USER fluent
