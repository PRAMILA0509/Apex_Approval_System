
// Base API URL (your Spring Boot backend)
const API_BASE_URL = "http://localhost:8080/api/forms";

// Get auth headers from localStorage
function getAuthHeaders() {
  const username = localStorage.getItem("username");
  const password = localStorage.getItem("password");

  if (!username || !password) {
    alert("Please login again!");
    window.location.href = "login.html";
    return null;
  }

  return {
    "Authorization": "Basic " + btoa(username + ":" + password),
    "Content-Type": "application/json",
  };
}

//  Login check (used in script.js)
async function loginUser(username, password) {
  try {
    const res = await fetch(API_BASE_URL, {
      method: "GET",
      headers: {
        "Authorization": "Basic " + btoa(username + ":" + password),
      },
    });

    if (res.ok) {
      localStorage.setItem("username", username);
      localStorage.setItem("password", password);
      return true;
    }
    return false;
  } catch (err) {
    console.error("Login failed:", err);
    return false;
  }
}

//  Fetch all forms
async function getAllForms() {
  const headers = getAuthHeaders();
  if (!headers) return;

  const res = await fetch(API_BASE_URL, { headers });
  return await res.json();
}

//  Create a new form (Faculty)
async function createForm(formData) {
  const headers = getAuthHeaders();
  if (!headers) return;

  const res = await fetch(API_BASE_URL, {
    method: "POST",
    headers,
    body: JSON.stringify(formData),
  });

  return await res.json();
}

//  Forward form (HOD)
async function forwardForm(id, forwarderName) {
  const headers = getAuthHeaders();
  if (!headers) return;

  const res = await fetch(`${API_BASE_URL}/${id}/forward?forwarderName=${encodeURIComponent(forwarderName)}`, {
    method: "PUT",
    headers,
  });
  return await res.json();
}

//  Recommend form (Dean)
async function recommendForm(id, recommenderName) {
  const headers = getAuthHeaders();
  if (!headers) return;

  const res = await fetch(`${API_BASE_URL}/${id}/recommend?recommenderName=${encodeURIComponent(recommenderName)}`, {
    method: "PUT",
    headers,
  });
  return await res.json();
}

//  Approve or reject form (Admin)
async function approveForm(id, approverName, remarks, approved = true) {
  const headers = getAuthHeaders();
  if (!headers) return;

  const res = await fetch(
    `${API_BASE_URL}/${id}/approve?approverName=${encodeURIComponent(
      approverName
    )}&remarks=${encodeURIComponent(remarks)}&approved=${approved}`,
    { method: "PUT", headers }
  );
  return await res.json();
}

// Logout
function logoutUser() {
  localStorage.removeItem("username");
  localStorage.removeItem("password");
  window.location.href = "login.html";
}
