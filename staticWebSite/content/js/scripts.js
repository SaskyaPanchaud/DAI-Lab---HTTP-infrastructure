window.addEventListener('DOMContentLoaded', event => {

    // Navbar shrink function
    let navbarShrink = function () {
        const navbarCollapsible = document.body.querySelector('#mainNav');
        if (!navbarCollapsible) {
            return;
        }
        if (window.scrollY === 0) {
            navbarCollapsible.classList.remove('navbar-shrink')
        } else {
            navbarCollapsible.classList.add('navbar-shrink')
        }

    };

    // Shrink the navbar 
    navbarShrink();

    // Shrink the navbar when page is scrolled
    document.addEventListener('scroll', navbarShrink);

    // Activate Bootstrap scrollspy on the main nav element
    const mainNav = document.body.querySelector('#mainNav');
    if (mainNav) {
        new bootstrap.ScrollSpy(document.body, {
            target: '#mainNav',
            rootMargin: '0px 0px -40%',
        });
    }

    // Collapse responsive navbar when toggler is visible
    const navbarToggler = document.body.querySelector('.navbar-toggler');
    const responsiveNavItems = [].slice.call(
        document.querySelectorAll('#navbarResponsive .nav-link')
    );
    responsiveNavItems.map(function (responsiveNavItem) {
        responsiveNavItem.addEventListener('click', () => {
            if (window.getComputedStyle(navbarToggler).display !== 'none') {
                navbarToggler.click();
            }
        });
    });
});

function displayList() {
    // FIXME : fetch = appel HTTP (et si sans parametre alors get)
    fetch('http://localhost/api/quotes')
        .then(function (response) {
            // The API call was successful !
            return response.json();
        })
        .then(function (data) {
            // This is the JSON from our response
            let div = document.getElementById("listQuotes");
            div.innerHTML = "";
            for (let i in data) {
                let span = document.createElement("span");
                span.textContent = "\"" + data[i].quote + "\", " + data[i].author;
                div.append(span);
                div.append(document.createElement("br"));
            }
        })
        .catch(function (err) {
            //There was an error
            console.warn('Something went wrong.', err);
        });
}