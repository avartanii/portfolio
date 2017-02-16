/*
  A sample jQuery plug-in: this one converts the selected elements into a 3D
  “swivel” control.
  This plugin's options object can include:
    change: function () { }
    - Callback for whenever the control has been manipulated.
*/

(($) => {
    $.fn.slider = function (options) {
        let $this = this;
        let $current = null;
        let anchorX = 0;
        let max = parseInt(this.attr("data-max")) || 10;
        let min = parseInt(this.attr("data-min")) || 0;

        $this.addClass("slider").mousedown((event) => {
            $current = $(event.currentTarget);
            anchorX = event.screenX - ($current.data('slider-position') || 0);
        });

        // Other mouse events go at the level of the document because
        // they might leave the element's bounding box.
        $(document).mousemove((event) => {
            if ($current) {
                let parentOffset = $this.parent().offset().left || 0;
                let parentWidth = $this.parent().width() || 1;
                let width = $this.width() || 0;
                let widthDiff = parentWidth - width !== 0 ? parentWidth - width : 1;
                let currentPosition = $current.data('slider-position') || 0;
                let updatedPosition = event.screenX - anchorX;
                let newPosition = 0;
                let newValue = min;
                
                if (updatedPosition + parentOffset < parentOffset) {
                    newValue = min;
                } else if (updatedPosition + width > parentWidth) {
                    newValue = max;
                } else {
                    newValue = Math.round(updatedPosition / widthDiff * max) + min;
                    newValue = newValue > max ? max : newValue;
                }

                newPosition = Math.round((parentWidth - width) / (max - min) * (newValue - min));
                let newCSS = "translateX(" + newPosition + "px)";

                $current.css({
                    'transform': newCSS
                }).data({
                    'slider-position': newPosition,
                    'slider-value': newValue
                });

                // Invoke the callback. We want jQuery-like behavior that binds `this` to the component
                // that change, so we use `call` instead of plain parentheses.
                if ($.isFunction(options.change)) {
                    options.change.call($current, currentPosition, newPosition, max);
                }
            }
        }).mouseup(() => {
            $current = null;
        });

        return $this;
    };
})(jQuery);