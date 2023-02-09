data "docker_registry_image" "service" {
  name = format(
    "%s-docker.pkg.dev/%s/cloud-run-source-deploy/%s",
    var.project_region,
    var.project_id,
    var.service_name,
  )
}

resource "google_cloud_run_service" "endpoint" {
  depends_on = [null_resource.openapi_proxy_image]
  name       = "${var.resource_prefix}-endpoint"
  location   = var.project_region
  project    = var.project_id
  template {
    spec {
      containers {
        image = format(
          "%s/endpoints-runtime-serverless:%s-%s-%s",
          local.endpoints_registry, var.esp_tag, var.service_name,
          google_endpoints_service.endpoints.config_id,
        )
      }
    }
  }
}

resource "google_cloud_run_service" "service" {
  name                       = "${var.resource_prefix}-service"
  location                   = var.project_region
  autogenerate_revision_name = true

  template {
    spec {
      containers {
        image = "${data.docker_registry_image.service.name}@${data.docker_registry_image.service.sha256_digest}"
      }
    }
  }

  traffic {
    latest_revision = true
    percent         = 100
  }
}

resource "google_cloud_run_service_iam_policy" "noauth-endpoints" {
  location    = google_cloud_run_service.endpoint.location
  project     = google_cloud_run_service.endpoint.project
  policy_data = data.google_iam_policy.noauth.policy_data
  service     = google_cloud_run_service.endpoint.name
}
