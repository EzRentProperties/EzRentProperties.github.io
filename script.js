document.addEventListener("DOMContentLoaded", () => {
    const searchInput = document.querySelector(".search-bar input[type='text']");
    const propertyContainer = document.querySelector(".container");

    // Existing properties
    const properties = [
        { name: "Spacious House", location: "BKC,Opp. Main bridge,Sion", price: "Rs.25,000/month", img: "pexels-photo.jpg" },
        { name: "Comfortable Apartment", location: "Station Road,Somaiya,Chunnabhati", price: "Rs.20,000/month", img: "photo.jpg" },
        { name: "Modern Flat", location: "Metro Road.Andheri", price: "Rs.30,000/month", img: "House.jpg" }
    ];

    // New properties for specific locations
    const newProperties = [
        { name: "Budget Room near Somaiya College", location: "Somaiya College, Sion", price: "Rs.15,000/month", img: "download.jpg" },
        { name: "Luxury Flat in Vidyavihar", location: "Vidyavihar", price: "Rs.35,000/month", img: "download.jpg" },
        { name: "Modern Flat near DY Patil", location: "DY Patil, Navi Mumbai", price: "Rs.25,000/month", img: "download.jpg" },
        { name: "Affordable Apartment near NMIMS", location: "NMIMS, Mumbai", price: "Rs.18,000/month", img: "download.jpg" },
        { name: "Shared Room near Sion Station", location: "Sion Station", price: "Rs.12,000/month", img: "download.jpg" },
        { name: "1 BHK near Somaiya College", location: "Somaiya College, Sion", price: "Rs.22,000/month", img: "download.jpg" }
    ];

    // Function to display property cards based on search results
    function displayProperties(searchTerm) {
        // Clear any existing property cards
        const existingCards = document.querySelectorAll(".property-card");
        existingCards.forEach(card => card.remove());

        // Combine all properties
        const allProperties = [...properties, ...newProperties];

        // Filter properties based on search term
        const filteredProperties = allProperties.filter(property =>
            property.name.toLowerCase().includes(searchTerm) ||
            property.location.toLowerCase().includes(searchTerm)
        );

        // Display each matched property
        filteredProperties.forEach(property => {
            const propertyCard = document.createElement("div");
            propertyCard.classList.add("property-card");

            propertyCard.innerHTML = `
                <img src="${property.img}" alt="${property.name}">
                <h2>${property.name}</h2>
                <p class="details"><i class="location-icon">📍</i> ${property.location}</p>
                <p class="details">Price: ${property.price}</p>
                <a href="#" class="contact">Contact Owner</a>
            `;

            propertyContainer.appendChild(propertyCard);
        });

        // If no properties match, show a "No results found" message
        if (filteredProperties.length === 0) {
            const noResults = document.createElement("p");
            noResults.textContent = "No properties match your search.";
            propertyContainer.appendChild(noResults);
        }
    }

    // Event listener for the search bar input
    searchInput.addEventListener("input", () => {
        const searchTerm = searchInput.value.trim().toLowerCase();
        displayProperties(searchTerm);
    });
});
