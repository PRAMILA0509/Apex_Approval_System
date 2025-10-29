// ============================
//  script.js - UI logic
// ============================

// ---------- LOGIN PAGE ----------
if (document.getElementById("loginForm")) {
  document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    const error = document.getElementById("error");

    try {
      // Explicit CORS mode and include credentials to match server settings
      const res = await fetch("http://localhost:8080/api/forms", {
        method: "GET",
        mode: "cors",
        credentials: "include",
        headers: {
          "Authorization": "Basic " + btoa(username + ":" + password),
          "Content-Type": "application/json"
        },
      });

      console.log("Login fetch response:", res.status, res.statusText);

      if (res.ok) {
        // Save login credentials for future API calls
        localStorage.setItem("username", username);
        localStorage.setItem("password", password);

        // Redirect based on role
        if (username.startsWith("faculty")) {
          window.location.href = "index.html";
        } else {
          window.location.href = "dashboard.html";
        }
      } else {
        const body = await res.text().catch(() => "(no body)");
        console.warn("Login failed (response not ok):", res.status, body);
        error.style.display = "block";
        error.textContent = "Invalid credentials or server error.";
      }
    } catch (err) {
      console.error("Login failed (network/CORS):", err.name, err.message);
      error.style.display = "block";
      error.textContent = "Server not reachable or CORS blocked. Check backend and DevTools Network tab.";
    }
  });
}

// ---------- DASHBOARD ----------
if (document.getElementById("logoutBtn")) {
  document.getElementById("logoutBtn").addEventListener("click", logoutUser);
}

if (document.getElementById("formTable")) {
  window.addEventListener("DOMContentLoaded", async () => {
    try {
      const forms = await getAllForms();
      renderForms(forms);
    } catch (err) {
      console.error("Failed to load forms:", err);
    }
  });
}

function renderForms(forms) {
  const tableBody = document.getElementById("formTableBody");
  tableBody.innerHTML = "";

  if (!forms || forms.length === 0) {
    tableBody.innerHTML = "<tr><td colspan='5'>No forms available</td></tr>";
    return;
  }

  forms.forEach((f) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${f.id}</td>
      <td>${f.subject || "-"}</td>
      <td>${f.status}</td>
      <td>${f.forwardedBy || "-"}</td>
      <td>
        ${getActionButtons(f)}
      </td>
    `;
    tableBody.appendChild(tr);
  });
}

function getActionButtons(form) {
  const username = localStorage.getItem("username") || "";
  let btns = "";

  if (username.startsWith("hod") && form.status === "SUBMITTED") {
    btns = `<button onclick="forwardForm(${form.id}, '${username}')">Forward</button>`;
  } else if (username.startsWith("dean") && form.status === "FORWARDED") {
    btns = `<button onclick="recommendForm(${form.id}, '${username}')">Recommend</button>`;
  } else if (username.startsWith("principal") && form.status === "RECOMMENDED") {
    btns = `
      <button onclick="approveForm(${form.id}, '${username}', 'Approved', true)">Approve</button>
      <button onclick="approveForm(${form.id}, '${username}', 'Rejected', false)">Reject</button>
    `;
  }

  return btns || "-";
}

// ---------- FORM SUBMISSION ----------
if (document.getElementById("formSubmit")) {
  document.getElementById("formSubmit").addEventListener("submit", async (e) => {
    e.preventDefault();

    const formData = {
      facultyName: document.getElementById("facultyName").value.trim(),
      facultyId: document.getElementById("facultyId").value.trim(),
      departmentOrClub: document.getElementById("departmentOrClub").value.trim(),
      subject: document.getElementById("subject").value.trim(),
      description: document.getElementById("description").value.trim(),
      amountRequired: parseFloat(document.getElementById("amountRequired").value || 0),
      expectedOutcome: document.getElementById("expectedOutcome").value.trim(),
    };

    try {
      const res = await fetch("http://localhost:8080/api/forms", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Basic " + btoa(
            localStorage.getItem("username") + ":" + localStorage.getItem("password")
          ),
        },
        body: JSON.stringify(formData),
      });

      const responseBox = document.getElementById("response");
      if (res.ok) {
        responseBox.textContent = "Form submitted successfully!";
        responseBox.style.color = "green";
        e.target.reset();
      } else {
        const errorText = await res.text();
        responseBox.textContent = `Submission failed: ${errorText}`;
        responseBox.style.color = "red";
      }
    } catch (err) {
      console.error("Form submission failed:", err);
      const responseBox = document.getElementById("response");
      responseBox.textContent = "Server not reachable or CORS issue.";
      responseBox.style.color = "red";
    }
  });
}

