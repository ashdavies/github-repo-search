terraform {
  required_providers {
    docker = {
      source = "kreuzwerker/docker"
      version = "3.0.2"
    }

    github = {
      source  = "integrations/github"
      version = "6.2.2"
    }

    google = {
      source  = "hashicorp/google"
      version = "5.35.0"
    }
  }

  required_version = "1.8.5"
}
