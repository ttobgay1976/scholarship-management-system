
//this is use for Country State and City for the registration page

document.addEventListener('DOMContentLoaded', () => {
    const countrySelect = document.getElementById('countrySelect');
    const stateSelect = document.getElementById('stateSelect');
    const citySelect = document.getElementById('citySelect');

    if (!countrySelect || !stateSelect || !citySelect) return;

    const selectedStateId = stateSelect.getAttribute('data-selected');
    const selectedCityId = citySelect.getAttribute('data-selected');

    function loadStates(countryId, selectedStateId = null, selectedCityId = null) {
        stateSelect.innerHTML = '<option value="">--Select State--</option>';
        citySelect.innerHTML = '<option value="">--Select City--</option>';

        if (!countryId) return;

        fetch(`/api/states?countryId=${countryId}`)
            .then(res => res.json())
            .then(states => {
                states.forEach(state => {
                    const option = document.createElement('option');
                    option.value = state.id;
                    option.textContent = state.stateName;

                    if (selectedStateId && String(state.id) === String(selectedStateId)) {
                        option.selected = true;
                    }

                    stateSelect.appendChild(option);
                });

                if (selectedStateId) {
                    loadCities(selectedStateId, selectedCityId);
                }
            })
            .catch(err => console.error(err));
    }

    function loadCities(stateId, selectedCityId = null) {
        citySelect.innerHTML = '<option value="">--Select City--</option>';

        if (!stateId) return;

        fetch(`/api/cities?stateId=${stateId}`)
            .then(res => res.json())
            .then(cities => {
                cities.forEach(city => {
                    const option = document.createElement('option');
                    option.value = city.id;
                    option.textContent = city.cityName;

                    if (selectedCityId && String(city.id) === String(selectedCityId)) {
                        option.selected = true;
                    }

                    citySelect.appendChild(option);
                });
            })
            .catch(err => console.error(err));
    }

    // New entry: when user changes country
    countrySelect.addEventListener('change', () => {
        const countryId = countrySelect.value;
        loadStates(countryId);
    });

    // New entry: when user changes state
    stateSelect.addEventListener('change', () => {
        const stateId = stateSelect.value;
        loadCities(stateId);
    });

    // Edit mode: auto-load state/city if country already selected
    if (countrySelect.value) {
        loadStates(countrySelect.value, selectedStateId, selectedCityId);
    }
});
