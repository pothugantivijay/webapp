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
  default     = "centos-stream-8-v20240110"
}

variable "zone" {
  description = "The zone to use for the instance"
  default     = "us-central1-a"
}

// variable "OpsConfig_file" {
//   description = "The ops config file"
//   default     = ""
// }

source "googlecompute" "custom_image" {
  project_id   = var.project_id
  source_image = var.source_image
  zone         = var.zone
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

  provisioner "file" {
    source      = "OpsAgentConfig.yaml"
    destination = "/tmp/OpsAgentConfig.yaml"
  }

  provisioner "shell" {
    script = "mariadb_setup.sh"
  }
}
