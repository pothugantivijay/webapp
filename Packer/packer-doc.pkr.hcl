packer {
  required_plugins {
    googlecompute = {
      source  = "github.com/hashicorp/googlecompute"
      version = "~> 1"
    }
  }
}
//projectId
variable "project_id" {
  description = "The ID of the Google Cloud Platform project"
  default     = "devproject-414915"
}

variable "credentials" {
  description = "Path to the Google Cloud service account key file"
  default     = ""
}

variable "tmp_service_file" {
  type    = string
  default = ""
}

variable "source_file" {
  type    = string
  default = ""
}

variable "source_image" {
  description = "The source image to use for the custom image"
  default     = "centos-cloud/centos-8"
}

variable "machine_type" {
  description = "The machine type for the instance"
  default     = "n1-standard-1"
}

variable "zone" {
  description = "The zone to use for the instance"
  default     = "us-central1-a"
}

variable "network" {
  description = "The network to use for the instance"
  default     = "default"
}

source "googlecompute" "custom_image" {
  project_id       = var.project_id
  source_image     = var.source_image
  zone             = var.zone
  machine_type     = var.machine_type
  network          = var.network
  credentials_file = var.credentials

  ssh_username = "packer"
}

build {
  sources = ["source.googlecompute.custom_image"]

  provisioner "file" {
    source      = var.tmp_service_file
    destination = "/tmp/systemd-service.service"
  }

  provisioner "file" {
    source      = var.source_file
    destination = "/tmp/assignment-0.0.1-SNAPSHOT.jar"
  }

  provisioner "shell" {
    script = "C:/Users/vijay/OneDrive/Desktop/cloudassignment3/webapp/Packer/mariadb_setup.sh"
  }
}
