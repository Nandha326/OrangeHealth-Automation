/* ============================================================
   OrangeHealth Automation — Custom JS Enhancements
   ExtentReports v5 | custom-spark-script.js
   ============================================================ */

(function () {
  'use strict';

  /* ── Wait for DOM ready ─────────────────────────────────── */
  function ready(fn) {
    if (document.readyState !== 'loading') { fn(); }
    else { document.addEventListener('DOMContentLoaded', fn); }
  }

  /* ── Retry helper (Extent renders async via Angular) ────── */
  function retryUntil(fn, maxMs, intervalMs) {
    maxMs      = maxMs      || 8000;
    intervalMs = intervalMs || 300;
    var elapsed = 0;
    var timer = setInterval(function () {
      fn();
      elapsed += intervalMs;
      if (elapsed >= maxMs) { clearInterval(timer); }
    }, intervalMs);
  }

  /* ================================================================
     1. LIGHTBOX — full-screen screenshot overlay
     ================================================================ */
  function initLightbox() {
    if (document.getElementById('oh-lightbox')) { return; }

    var overlay = document.createElement('div');
    overlay.id  = 'oh-lightbox';

    var closeBtn = document.createElement('span');
    closeBtn.id  = 'oh-lightbox-close';
    closeBtn.innerHTML = '&#x2715;';
    closeBtn.title = 'Close (Esc)';

    var img = document.createElement('img');
    img.id  = 'oh-lightbox-img';
    img.alt = 'Screenshot';

    overlay.appendChild(closeBtn);
    overlay.appendChild(img);
    document.body.appendChild(overlay);

    function open(src) {
      img.src = src;
      overlay.classList.add('active');
      document.body.style.overflow = 'hidden';
    }

    function close() {
      overlay.classList.remove('active');
      document.body.style.overflow = '';
      img.src = '';
    }

    closeBtn.addEventListener('click', function (e) { e.stopPropagation(); close(); });
    overlay.addEventListener('click', function (e) { if (e.target === overlay) { close(); } });
    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' && overlay.classList.contains('active')) { close(); }
    });

    /* Delegate — intercept screenshot clicks anywhere in the report */
    document.addEventListener('click', function (e) {
      var el = e.target;
      if (
        el.tagName === 'IMG' &&
        (el.closest('.screenshot-container') ||
         el.closest('.media-container') ||
         el.classList.contains('screenshot') ||
         el.src.match(/\.(png|jpg|jpeg|gif|webp)/i))
      ) {
        e.preventDefault();
        e.stopPropagation();
        open(el.src);
      }
    }, true);
  }

  /* ================================================================
     2. EXCEPTION TOGGLE — collapse long stack traces
     ================================================================ */
  var TRACE_LINE_THRESHOLD = 8;

  function collapseTraces() {
    var blocks = document.querySelectorAll(
      '.exception-detail, pre.exception, .stack-trace, code.exception, pre'
    );

    blocks.forEach(function (block) {
      if (block.dataset.ohTraceInit) { return; }
      block.dataset.ohTraceInit = '1';

      var lines = block.innerText.split('\n');
      if (lines.length <= TRACE_LINE_THRESHOLD) { return; }

      var preview  = lines.slice(0, TRACE_LINE_THRESHOLD).join('\n');
      var overflow = lines.slice(TRACE_LINE_THRESHOLD).join('\n');

      var previewNode   = document.createTextNode(preview);
      var overflowSpan  = document.createElement('span');
      overflowSpan.className = 'oh-trace-collapsed';
      overflowSpan.textContent = '\n' + overflow;

      var toggle = document.createElement('button');
      toggle.className = 'oh-trace-toggle';
      toggle.innerHTML = '<span class="oh-toggle-icon">&#9660;</span> Expand Stack Trace ('
        + (lines.length - TRACE_LINE_THRESHOLD) + ' more lines)';

      var collapsed = true;

      toggle.addEventListener('click', function (e) {
        e.stopPropagation();
        collapsed = !collapsed;
        if (collapsed) {
          overflowSpan.classList.add('oh-trace-collapsed');
          toggle.innerHTML = '<span class="oh-toggle-icon">&#9660;</span> Expand Stack Trace ('
            + (lines.length - TRACE_LINE_THRESHOLD) + ' more lines)';
        } else {
          overflowSpan.classList.remove('oh-trace-collapsed');
          toggle.innerHTML = '<span class="oh-toggle-icon">&#9650;</span> Collapse Stack Trace';
        }
      });

      block.textContent = '';
      block.appendChild(previewNode);
      block.appendChild(overflowSpan);
      block.parentNode.insertBefore(toggle, block.nextSibling);
    });
  }

  /* ================================================================
     3. KEYBOARD SHORTCUTS
        f → filter failed tests
        p → filter passed tests
        d → dashboard view
        / → focus search
     ================================================================ */
  function initKeyboardShortcuts() {
    document.addEventListener('keydown', function (e) {
      /* Ignore when typing in an input */
      var tag = document.activeElement && document.activeElement.tagName;
      if (tag === 'INPUT' || tag === 'TEXTAREA' || tag === 'SELECT') { return; }

      switch (e.key.toLowerCase()) {

        case 'f':
          clickNavItem('fail');
          break;

        case 'p':
          clickNavItem('pass');
          break;

        case 'd':
          clickNavItem('dashboard');
          break;

        case '/':
          e.preventDefault();
          var searchInput = document.querySelector(
            'input[type="search"], .search-input, #searchInput, input[placeholder*="earch"]'
          );
          if (searchInput) { searchInput.focus(); }
          break;
      }
    });
  }

  function clickNavItem(keyword) {
    var links = document.querySelectorAll('#nav-left a, .nav-left a, nav a');
    for (var i = 0; i < links.length; i++) {
      var text = links[i].textContent.trim().toLowerCase();
      if (text.indexOf(keyword) !== -1) {
        links[i].click();
        return;
      }
    }
    /* Fallback: try filter buttons / tabs */
    var btns = document.querySelectorAll('[data-filter], .filter-btn, .tab-btn');
    for (var j = 0; j < btns.length; j++) {
      var bText = (btns[j].dataset.filter || btns[j].textContent).toLowerCase();
      if (bText.indexOf(keyword) !== -1) { btns[j].click(); return; }
    }
  }

  /* ================================================================
     4. SHORTCUT HINT BAR
     ================================================================ */
  function initShortcutBar() {
    if (document.getElementById('oh-shortcut-bar')) { return; }

    var bar = document.createElement('div');
    bar.id  = 'oh-shortcut-bar';
    bar.innerHTML =
      '<kbd>f</kbd> Failed &nbsp;' +
      '<kbd>p</kbd> Passed &nbsp;' +
      '<kbd>d</kbd> Dashboard &nbsp;' +
      '<kbd>/</kbd> Search &nbsp;' +
      '<kbd>Esc</kbd> Close';

    document.body.appendChild(bar);

    /* Auto-hide after 6 s */
    setTimeout(function () {
      bar.style.transition = 'opacity 0.6s';
      bar.style.opacity    = '0';
      setTimeout(function () { bar.style.display = 'none'; }, 700);
    }, 6000);
  }

  /* ================================================================
     5. BOOT
     ================================================================ */
  ready(function () {
    initLightbox();
    initKeyboardShortcuts();
    initShortcutBar();

    /* Extent renders content asynchronously — poll for new nodes */
    retryUntil(collapseTraces, 10000, 400);

    /* Also observe DOM mutations for dynamically loaded test details */
    if (window.MutationObserver) {
      var observer = new MutationObserver(function () {
        collapseTraces();
      });
      observer.observe(document.body, { childList: true, subtree: true });
    }
  });

})();
