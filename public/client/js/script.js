/**
 * Main script for client page.
 * @author Esaú García (EgaTuts).
 * @version 1.0.0
 */
(function (root, DOC, M, Mustache, Masonry, io) {
  "use strict";
  /*
   * We start defining our custom methods.
   */

  /**
   * Returns a random value between the specified range.
   * @param {number} min The minimum value that can be returned.
   * @param {number} max The maximum value that can be returned.
   * @return {number} Random value between the given range.
   */
  var
    _range = function (min, max) {
      return (max - min) * Math.random() + min;
    },

    /**
     * Sets the maximum number of decimals a number can have.
     * @param {number} num The number to strip.
     * @param {number} round The maximum decimal precision.
     * @return {number} The given number with given fixed length.
     */
    _fixed = function (num, round) {
      return (num * Math.pow(10, round) | 0) / Math.pow(10, round);
    },

    /**
     * Transforms a string containing HTML to an HTML element.
     * @param {string} html The string containing all the HTML.
     * @return {Element} The given string as an HTML element.
     */
    _toHTML = (function (elem) {
      var child = null;
      return function (html) {
        elem.innerHTML = html;
        child = elem.children[0];
        elem.innerHTML = "";
        return child;
      };
    })(DOC.createElement("div")),

    /**
     * Debounces a function to execute after a certain time that resets the timer if called more than once before being executed.
     * @param {function} fn The function to debounce.
     * @param {number} time The time to debounce in miliseconds.
     * @param {boolean} immediate Specifies if it must be executed instantly.
     */
    _debounce = (function () {
      var
        timeout = null,
        context = null,
        args    = null,
        func    = null,
        instant = null,
        later = function () {
          timeout = null;
          if ( !instant ) func.apply(context, args);
        };

      return function (fn, time, immediate) {

        return function () {
          context = this,
          args    = arguments,
          func    = fn;
          instant = !!immediate && !timeout;
          clearTimeout(timeout);
          timeout = setTimeout(later, time);
          if (instant) func.apply(context, args);
        };

      };
    })(),

    /**
     * Renders a mustache template.
     * @param {string} data The data to render with Mustache.
     * @param {object} settings Custom data to render in the template.
     * @return {element} The rendered template returned by Mustache as string as HTML.
     */
    _render = function (data, settings) {
      return _toHTML(Mustache.to_html(data, settings));
    },

    /*
     * Local variables.
     */
    container = DOC.getElementById("container"),
    template  = DOC.getElementById("host-template"),

    /**
     * Renders the host template.
     * @param {object} settings Object containing the data to render.
     * @return {element} The rendered template.
     * @see _render
     */
    renderTemplate = function (settings) {
      return _render(template.innerHTML, settings);
    },
    
    /*
     * The Masonry main instance.
     */
    
    len         = 0,
    columnWidth = 280,
    gutter      = 32,
    maxWidth    = null,
    maxColumns  = null,
    tempColumns = null,
    tempWidth   = null,
    
    /*
     * Returns the width that takes up a n number of columns.
     * 
     * f(x) = |x| * 280 + (|x| - 1) * 32, x € N, x > 0
     */
    
    /**
     * Returns the number of columns that can fit up in the given width. Is the equivalent of f'(x) = (x + 32) / 312.
     * 
     * @param {number} x The free space to fill with columns.
     * @return {number} A natural and integer number.
     */
    possibleColumns = function (x) {
      return ( (x + 32) / 312) | 0;
    },
    
    /**
     * Resizes the main container.
     * @param {element} parent The main container of all columns.
     * @param {element} element The new added element.
     * @param {number} length The number of elements.
     */
    containerResizer = function (parent, element, length) {
      len         = length,
      maxWidth    = root.innerWidth - gutter * 2,
      maxColumns  = possibleColumns(maxWidth),
      tempColumns = len > maxColumns ? maxColumns : len,
      tempWidth   = tempColumns * columnWidth + (tempColumns - 1) * gutter;
      parent.style.width = tempWidth + "px";
    },
    
    masonry = new Masonry(container, {
      itemSelector: ".host",
      layoutMode: "fitRows",
      columnWidth: columnWidth,
      gutter: gutter
    });
    
    Masonry.prototype.addElements = function (parent, elements) {
      for ( var i = 0; i < elements.length; i++ ) {
        this.trigger("beforeLayoutComplete", [parent, elements[i], this.items.length + 1]);
        parent.appendChild( elements[i] );
        this.appended( elements[i] );
        this.layout();
      }
    };

    /*
     * Assigning resizing functions.
     */
    masonry.on("beforeLayoutComplete", containerResizer);
    root.onresize = function () {
      containerResizer(container, null, masonry.items.length);
    };
    
    
    for (var i = 0; i < 6; i++) {
      var example = renderTemplate({
        email: "example@domain.com", 
        image: "https://localhost/img/example.png",
        name: "Example",
        location: "Spain",
        latitude: function () {
          return _fixed(_range(-90, 90), 7);
        },
        longitude: function () {
          return _fixed(_range(-180, 180), 7);
        }
      });

      (function (data, time) {
        window.setTimeout(function () {
          masonry.addElements(container, [data]);
        }, time);
      })(example, i * 3000);

    }

})(this, document, Math, Mustache, Masonry, io);
