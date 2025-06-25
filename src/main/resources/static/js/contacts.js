console.log("contacts.js");

const viewContactModal = document.getElementById("view_contact_modal");

// options with default values
const options = {
  placement: "bottom-right",
  backdrop: "dynamic",
  backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
  closable: true,
  onHide: () => {
    console.log("modal is hidden");
  },
  onShow: () => {
    console.log("modal is shown");
  },
  onToggle: () => {
    console.log("modal has been toggled");
  },
};

// instance options object
const instanceOptions = {
  id: "view_contact_modal",
  override: true,
};

const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal() {
  contactModal.show();
}

function closeContactModel() {
  contactModal.hide();
}

async function loadContactData(id) {
  console.log(id);

  try {
    const contact = await (
      await fetch(`http://localhost:5050/api/contacts/${id}`)
    ).json();
    console.log(contact);

    document.getElementById("contact_name").innerHTML = contact.name;
    document.getElementById("contact_email").innerHTML = contact.email;
    document.getElementById("contact_profile_image").src = contact.picture;
    document.querySelector("#contact_address").innerHTML = contact.address;
    // document.querySelector("#contact_phone").innerHTML = contact.phoneNumber;
    document.querySelector("#contact_phone_info").innerHTML =
      contact.phoneNumber;
    document.querySelector("#contact_about").innerHTML = contact.description;
    const contactFavorite = document.querySelector("#contact_favorite");
    if (contact.favorite) {
      contactFavorite.classList.remove("hidden");
      contactFavorite.innerHTML =
        "<i class='fa-solid fa-heart h-6 w-6 text-blue-600'></i><span class='pl-1'>Favorite</span>";
      document.getElementById("contact_group").innerHTML = "Favorite";
    } else {
      contactFavorite.classList.add("hidden");
      document.getElementById("contact_group").innerHTML = "None";
    }

    if (contact.websiteLink) {
      document.querySelector("#contact_website").href = contact.websiteLink;
      document.querySelector("#contact_website").target = "_blank";
      document.querySelector("#contact_website").innerHTML =
        contact.websiteLink;
    }

    if (contact.socialLink) {
      document.querySelector("#contact_social_link").href = contact.socialLink;
      document.querySelector("#contact_social_link").target = "_blank";
      document.querySelector("#contact_social_link").innerHTML =
        contact.socialLink;
    }

    openContactModal();
  } catch (error) {
    console.log("Error: ", error);
  }
}
