resource "google_cloud_run_service" "main" {
  depends_on = [null_resource.main]
  name       = var.service_name
  location   = var.location
  project    = var.project

  template {
    spec {
      containers {
        image = data.google_artifact_registry_docker_image.main.self_link
      }
    }
  }

  traffic {
    percent         = 100
    latest_revision = true
  }

  lifecycle {
    prevent_destroy = true
  }
}

resource "google_cloud_run_domain_mapping" "main" {
  name     = var.endpoint_name
  location = google_cloud_run_service.main.location

  metadata {
    namespace = var.project
  }

  spec {
    route_name = google_cloud_run_service.main.name
  }
}

data "google_artifact_registry_docker_image" "main" {
  depends_on    = [null_resource.main]
  location      = var.location
  repository_id = var.repository_id
  image_name    = var.image_name
}

resource "null_resource" "main" {
  provisioner "local-exec" {
    command = <<EOS
    bash ${path.module}/scripts/gcloud_build_image \
      -g ${var.image_repository} \
      -s ${var.endpoint_name} \
      -c ${var.config_id} \
      -p ${var.project} \
      -v ${var.esp_version}
    EOS
  }

  triggers = {
    config_id   = google_endpoints_service.main.config_id
    esp_version = data.external.esp_version.result.esp_full_version
  }
}

resource "google_endpoints_service" "main" {
  openapi_config = var.openapi_config
  service_name   = var.endpoint_name
  project        = var.project
}

data "external" "esp_version" {
  program = ["bash", "${path.module}/scripts/esp_full_version", var.esp_version]
}
