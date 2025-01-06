const TEAM_API_URL = "http://localhost:8080/team";
const PLAYER_API_URL = "http://localhost:8080/player";
let availablePlayers = [];
let selectedPlayers = [];
let currentPage = 0;
const pageSize = 10;

// Fetch and display teams with pagination and sorting
async function fetchTeams(page = 0) {
    currentPage = page;

    const sortName = document.getElementById("sortName").value;
    const sortAcronym = document.getElementById("sortAcronym").value;
    const sortBudget = document.getElementById("sortBudget").value;

    const response = await fetch(`${TEAM_API_URL}/list?page=${page}&pageSize=${pageSize}&nameSort=${sortName}&acronymSort=${sortAcronym}&budgetSort=${sortBudget}`);
    const data = await response.json();

    const tableBody = document.getElementById("teamsTable");
    tableBody.innerHTML = "";

    data.items.forEach(team => {
        const row = `
            <tr onclick="togglePlayers(${team.id})">
                <td><span id="arrow-${team.id}" class="arrow">&#9654;</span></td>
                <td>${team.id}</td>
                <td>${team.name}</td>
                <td>${team.acronym}</td>
                <td>${team.budget.toLocaleString()} €</td>
                <td><button class="btn btn-danger btn-sm" onclick="deleteTeam(${team.id}, event)">Delete</button></td>
            </tr>
            <tr id="players-${team.id}" class="players-row" style="display: none;">
                <td colspan="6">
                    <ul class="list-group">
                        ${team.players.map(player => `<li class="list-group-item">${player.name} (${player.position})</li>`).join("")}
                    </ul>
                </td>
            </tr>
        `;
        tableBody.innerHTML += row;
    });

    updatePaginationControls(data.page, data.pageCount);
    resetForm();
}

function updatePaginationControls(page, pageCount) {
    const paginationControls = document.getElementById("paginationControls");
    paginationControls.innerHTML = "";

    for (let i = 0; i < pageCount; i++) {
        paginationControls.innerHTML += `<button onclick="fetchTeams(${i})" ${i === page ? 'class="active"' : ''}>${i + 1}</button>`;
    }

    if (page < pageCount - 1) {
        paginationControls.innerHTML += `<button onclick="fetchTeams(${page + 1})">Next</button>`;
    }
}

// Fetch available players
async function fetchAvailablePlayers() {
    const response = await fetch(PLAYER_API_URL + "/available-players");
    availablePlayers = await response.json();
    updatePlayerSelect();
}

// Update the player dropdown list
function updatePlayerSelect() {
    const playerSelect = document.getElementById("playerSelect");
    playerSelect.innerHTML = availablePlayers.map(player => `<option value="${player.id}">${player.id} - ${player.name} (${player.position})</option>`).join("");
}

// Add selected player to the list
function addPlayer() {
    const playerSelect = document.getElementById("playerSelect");
    const selectedPlayerId = playerSelect.value;
    const selectedPlayer = availablePlayers.find(player => player.id == selectedPlayerId);

    if (selectedPlayer && !selectedPlayers.includes(selectedPlayer)) {
        selectedPlayers.push(selectedPlayer);
        availablePlayers = availablePlayers.filter(player => player.id != selectedPlayerId);
        updateSelectedPlayersList();
        updatePlayerSelect();
    }
}

// Remove selected player from the list
function removePlayer(playerId) {
    const player = selectedPlayers.find(player => player.id == playerId);
    if (player) {
        selectedPlayers = selectedPlayers.filter(player => player.id != playerId);
        availablePlayers.push(player);
        updateSelectedPlayersList();
        updatePlayerSelect();
    }
}

// Update the list of selected players
function updateSelectedPlayersList() {
    const selectedPlayersList = document.getElementById("selectedPlayers");
    selectedPlayersList.innerHTML = selectedPlayers.map(player =>
        `<li class="list-group-item">
            ${player.name} (${player.position})
            <button class="btn btn-danger btn-sm float-end" onclick="removePlayer(${player.id})">Remove</button>
        </li>`
    ).join("");
}

// Add a new team
async function addTeam() {
    const name = document.getElementById("teamName").value;
    const acronym = document.getElementById("teamAcronym").value;
    const budget = document.getElementById("teamBudget").value;

    if (!name || !acronym || !budget) {
        alert("Please fill in all fields.");
        return;
    }

    const team = {
        name,
        acronym,
        budget: parseInt(budget),
        players: selectedPlayers.map(player => ({ id: player.id, name: player.name, position: player.position }))
    };

    await fetch(TEAM_API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(team)
    });

    document.getElementById("teamName").value = "";
    document.getElementById("teamAcronym").value = "";
    document.getElementById("teamBudget").value = "";
    selectedPlayers = [];
    updateSelectedPlayersList();

    fetchTeams(); // Refresh the list
}

// Delete a team
async function deleteTeam(id, event) {
    event.stopPropagation(); // Prevent triggering the row click event
    if (confirm("Are you sure you want to delete this team?")) {
        await fetch(TEAM_API_URL + `/${id}`, { method: "DELETE" });
        fetchTeams(); // Refresh the list
        refreshTeamDroplist(); // Refresh the droplist
        fetchAvailablePlayers(); // Refresh the available players
    }
}

// Toggle the display of players
function togglePlayers(teamId) {
    const playersRow = document.getElementById(`players-${teamId}`);
    const arrow = document.getElementById(`arrow-${teamId}`);
    const isHidden = playersRow.style.display === "none";
    playersRow.style.display = isHidden ? "table-row" : "none";
    arrow.innerHTML = isHidden ? "&#9660;" : "&#9654;";
}

// Fetch and update the team droplist
async function refreshTeamDroplist() {
    const response = await fetch(`${TEAM_API_URL}/all`);
    const teams = await response.json();
    const droplist = document.getElementById('teamDroplist');
    droplist.innerHTML = ''; // Clear existing options
    teams.forEach(team => {
        const option = document.createElement('option');
        option.value = team.id;
        option.textContent = team.name;
        droplist.appendChild(option);
    });
}

// Reset the form fields
function resetForm() {
    document.getElementById("teamName").value = "";
    document.getElementById("teamAcronym").value = "";
    document.getElementById("teamBudget").value = "";
    selectedPlayers = [];
    updateSelectedPlayersList();
}

// Load teams and available players when the page loads
fetchTeams();
fetchAvailablePlayers();
