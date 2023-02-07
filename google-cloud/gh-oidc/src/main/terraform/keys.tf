resource "google_apikeys_key" "integration" {
  display_name = "Integration key (managed by Terraform)"
  depends_on   = [google_project_service.apikeys]
  provider     = google.impersonation
  project      = var.project_id
  name         = "integration"

  restrictions {
    api_targets {
      methods = ["accounts.signInWithCustomToken"]
      service = "identitytoolkit.googleapis.com"
    }
  }
}
