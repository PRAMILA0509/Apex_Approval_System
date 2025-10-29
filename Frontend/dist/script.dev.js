"use strict";

// ============================
//  script.js - UI logic
// ============================
// ---------- LOGIN PAGE ----------
if (document.getElementById("loginForm")) {
  document.getElementById("loginForm").addEventListener("submit", function _callee(e) {
    var username, password, error, ok;
    return regeneratorRuntime.async(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            e.preventDefault();
            username = document.getElementById("username").value.trim();
            password = document.getElementById("password").value.trim();
            error = document.getElementById("error");
            _context.next = 6;
            return regeneratorRuntime.awrap(loginUser(username, password));

          case 6:
            ok = _context.sent;

            if (ok) {
              // Redirect based on role/username
              if (username.startsWith("faculty")) {
                window.location.href = "index.html";
              } else {
                window.location.href = "dashboard.html";
              }
            } else {
              error.style.display = "block";
              error.textContent = "Invalid credentials or server offline.";
            }

          case 8:
          case "end":
            return _context.stop();
        }
      }
    });
  });
} // ---------- DASHBOARD ----------


if (document.getElementById("logoutBtn")) {
  document.getElementById("logoutBtn").addEventListener("click", logoutUser);
}

if (document.getElementById("formTable")) {
  window.addEventListener("DOMContentLoaded", function _callee2() {
    var forms;
    return regeneratorRuntime.async(function _callee2$(_context2) {
      while (1) {
        switch (_context2.prev = _context2.next) {
          case 0:
            _context2.prev = 0;
            _context2.next = 3;
            return regeneratorRuntime.awrap(getAllForms());

          case 3:
            forms = _context2.sent;
            renderForms(forms);
            _context2.next = 10;
            break;

          case 7:
            _context2.prev = 7;
            _context2.t0 = _context2["catch"](0);
            console.error("Failed to load forms:", _context2.t0);

          case 10:
          case "end":
            return _context2.stop();
        }
      }
    }, null, null, [[0, 7]]);
  });
}

function renderForms(forms) {
  var tableBody = document.getElementById("formTableBody");
  tableBody.innerHTML = "";

  if (!forms || forms.length === 0) {
    tableBody.innerHTML = "<tr><td colspan='5'>No forms available</td></tr>";
    return;
  }

  forms.forEach(function (f) {
    var tr = document.createElement("tr");
    tr.innerHTML = "\n      <td>".concat(f.id, "</td>\n      <td>").concat(f.subject || "-", "</td>\n      <td>").concat(f.status, "</td>\n      <td>").concat(f.forwardedBy || "-", "</td>\n      <td>\n        ").concat(getActionButtons(f), "\n      </td>\n    ");
    tableBody.appendChild(tr);
  });
}

function getActionButtons(form) {
  var username = localStorage.getItem("username") || "";
  var btns = "";

  if (username.startsWith("hod") && form.status === "SUBMITTED") {
    btns = "<button onclick=\"forwardForm(".concat(form.id, ", '").concat(username, "')\">Forward</button>");
  } else if (username.startsWith("dean") && form.status === "FORWARDED") {
    btns = "<button onclick=\"recommendForm(".concat(form.id, ", '").concat(username, "')\">Recommend</button>");
  } else if (username.startsWith("principal") && form.status === "RECOMMENDED") {
    btns = "\n      <button onclick=\"approveForm(".concat(form.id, ", '").concat(username, "', 'Approved', true)\">Approve</button>\n      <button onclick=\"approveForm(").concat(form.id, ", '").concat(username, "', 'Rejected', false)\">Reject</button>\n    ");
  }

  return btns || "-";
}