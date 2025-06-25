console.log("contacts.js");

const baseUrl = "http://localhost:5050";

// View contact Modal
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

function closeContactModal() {
  contactModal.hide();
}

async function loadContactData(id) {
  console.log(id);

  try {
    const contact = await (await fetch(`${baseUrl}/api/contacts/${id}`)).json();
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

// delete contact
function deleteContact(id) {
  const swalWithBootstrapButtons = Swal.mixin({
    customClass: {
      popup:
        "rounded-lg p-6 dark:bg-gray-800 dark:text-gray-100 bg-white text-gray-900",
      confirmButton:
        "px-3 py-2 text-center text-white rounded-lg bg-gray-800 hover:bg-gray-700 dark:bg-blue-600 dark:hover:bg-blue-700",
      cancelButton:
        "m-4 px-3 py-2 border-2 border-gray-700 text-center text-gray-700 rounded-lg hover:bg-gray-50 dark:bg-gray-700 dark:hover:bg-gray-800 dark:text-gray-400",
    },
    buttonsStyling: false,
  });
  swalWithBootstrapButtons
    .fire({
      title: "Do you want to delete the contact?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, delete it!",
      cancelButtonText: "No, cancel!",
      reverseButtons: true,
    })
    .then((result) => {
      if (result.isConfirmed) {
        const url = `${baseUrl}/user/contacts/delete/${id}`;
        window.location.replace(url);

        // swalWithBootstrapButtons.fire({
        //   title: "Deleted!",
        //   text: "Your contact has been deleted.",
        //   icon: "success",
        // });
      }
    });
}

// // View Delete Contact Modal
// var deleteContactId;

// const viewDeleteContactModal = document.getElementById("view_contact_modal");

// // options with default values
// const options2 = {
//   placement: "bottom-right",
//   backdrop: "dynamic",
//   backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
//   closable: true,
//   onHide: () => {
//     console.log("modal is hidden");
//   },
//   onShow: () => {
//     console.log("modal is shown");
//   },
//   onToggle: () => {
//     console.log("modal has been toggled");
//   },
// };

// // instance options object
// const instanceOptions2 = {
//   id: "view_delete_contact_modal",
//   override: true,
// };

// const deleteContactModal = new Modal(
//   viewDeleteContactModal,
//   options2,
//   instanceOptions2
// );

// function openDeleteContactModal() {
//   deleteContactModal.show();
// }

// function closeDeleteContactModal() {
//   deleteContactModal.hide();
// }

// function openApiDeleteContactModal(id) {
//   openDeleteContactModal();
//   deleteContactId = id;
// }

// async function apiDeleteContact() {
//   console.log("delete contact id: " + deleteContactId);
//   try {
//     const status = await fetch(
//       `${baseUrl}/api/contacts/delete/${deleteContactId}`
//     );

//     if (status) {
//       console.log("contact deleted");
//     } else {
//       console.log("Error deleting contact!");
//     }
//   } catch (error) {
//     console.log("Error: ", error);
//   }
//   closeDeleteContactModal();
//   window.location.replace(`${baseUrl}/user/contacts`);
// }
