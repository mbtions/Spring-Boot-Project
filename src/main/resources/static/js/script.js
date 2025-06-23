console.log("Script Loaded");

let currentTheme = getTheme();

// on html document page loading
document.addEventListener("DOMContentLoaded", () => {
  // initially setting the theme
  console.log(localStorage.getItem("theme"));
  toggleTheme();
});

document.addEventListener("load", () => {
  // initially setting the theme
  toggleTheme();
});

function toggleTheme() {
  // init: set to web
  // document.querySelector("html").classList.add(currentTheme);
  changePageTheme(currentTheme, currentTheme);

  // set listener to toggle the theme on button press
  const toggleButton = document.querySelector("#theme_change_button");

  toggleButton.addEventListener("click", () => {
    let oldTheme = currentTheme;

    if (currentTheme === "dark") {
      currentTheme = "light";
    } else {
      currentTheme = "dark";
    }

    changePageTheme(currentTheme, oldTheme);
  });
}

// set theme to local storage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

// get theme from local storage
function getTheme() {
  let theme = localStorage.getItem("theme");
  return theme ? theme : "light";
}

// change page theme
function changePageTheme(newTheme, oldTheme) {
  // update theme to local storage
  setTheme(newTheme);
  console.log("New theme set: " + newTheme);
  // remove the intial theme on toggle button press
  document.querySelector("html").classList.remove(oldTheme);
  console.log("Old Theme removed: " + oldTheme);
  // set theme to web also
  document.querySelector("html").classList.add(newTheme);
  console.log("New theme added: " + newTheme);
  // change the toggle button text
  document
    .querySelector("#theme_change_button")
    .querySelector("span").textContent = newTheme === "dark" ? "Light" : "Dark";
}
