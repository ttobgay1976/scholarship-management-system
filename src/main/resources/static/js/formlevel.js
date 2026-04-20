
/**This js is fot User Role Mapping form
	created on dt 11/04/2026
 */
	document.addEventListener("DOMContentLoaded", function () {
	//First combo selection
	
	    const userSelect = document.getElementById("userSelect");

	    userSelect.addEventListener("change", function () {
	        const userId = this.value;

	        // 1. Uncheck all roles first
	        document.querySelectorAll("input[name='roleIds']")
	            .forEach(cb => cb.checked = false);

	        if (!userId) return;
	        // 2. Fetch assigned roles
	        fetch('/api/userroles?userId=' + userId)
	            .then(res => res.json())
	            .then(roleIds => {

	                roleIds.forEach(roleId => {
	                    const checkbox = document.getElementById("role-" + roleId);
	                    if (checkbox) {
	                        checkbox.checked = true;
	                    }
	                });

	            })
	            .catch(err => console.error("Error:", err));
	    });
	    
	    
	    //this os for second checkbox validation
	    
	    document.getElementById("userSelect").addEventListener("change", function () {

	        const userId = this.value;
		
	        if (!userId) return;
	    	//alert("@@@User selected :"+ userId);
	        fetch(`/api/user/${userId}/roles`)
	            .then(response => response.json())
	            .then(data => {

	                // Uncheck all first
	                document.querySelectorAll(".role-checkbox").forEach(cb => cb.checked = false);

	                // Check assigned roles
	                data.forEach(roleId => {
	                    const checkbox = document.querySelector(`.role-checkbox[value='${roleId}']`);
	                    if (checkbox) checkbox.checked = true;
	                });
	            });
	    });
});

