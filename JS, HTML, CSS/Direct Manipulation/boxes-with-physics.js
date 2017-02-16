(($) => {

    trashBoundX = 85;
    trashBoundY = 85;

    let boxesInProgress = { };

    /**
     * Tracks a box as it is rubberbanded or moved across the drawing area.
     */
    let trackDrag = (event) => {
        $.each(event.changedTouches, function (index, touch) {
            // Don't bother if we aren't tracking anything.
            if (touch.target.movingBox) {
                // Reposition the object.
                let newPosition = {
                    left: touch.pageX - touch.target.deltaX,
                    top: touch.pageY - touch.target.deltaY
                };

                if (touch.pageX < trashBoundX && touch.pageY < trashBoundY) {
                    $(touch.target).addClass("box-highlight-trash");
                } else {
                    $(touch.target).removeClass("box-highlight-trash");
                    $(touch.target).addClass("box-highlight");
                }

                $(touch.target).data('position', newPosition);
                touch.target.movingBox.offset(newPosition);
            } else if (touch.target.zoomingBox) {
                let newOffset = {
                    left: (touch.target.zoomingBox.anchorX < touch.pageX) ? touch.target.zoomingBox.anchorX : touch.pageX,
                    top: (touch.target.zoomingBox.anchorY < touch.pageY) ? touch.target.zoomingBox.anchorY : touch.pageY
                };

                touch.target.zoomingBox
                    .offset(newOffset)
                    .width(Math.abs(touch.pageX - touch.target.zoomingBox.anchorX))
                    .height(Math.abs(touch.pageY - touch.target.zoomingBox.anchorY))
                    .data('position', newOffset);

            } else if (boxesInProgress[touch.identifier]) {
                let newOffset = {
                    left: (boxesInProgress[touch.identifier].anchorX < touch.pageX) ? boxesInProgress[touch.identifier].anchorX : touch.pageX,
                    top: (boxesInProgress[touch.identifier].anchorY < touch.pageY) ? boxesInProgress[touch.identifier].anchorY : touch.pageY
                };

                boxesInProgress[touch.identifier].drawingBox
                    .offset(newOffset)
                    .width(Math.abs(touch.pageX - boxesInProgress[touch.identifier].anchorX))
                    .height(Math.abs(touch.pageY - boxesInProgress[touch.identifier].anchorY))
                    .data('position', newOffset);

            }
        });

        // Don't do any touch scrolling.
        event.preventDefault();
    };

    /**
     * Concludes a drawing or moving sequence.
     */
    let endDrag = (event) => {
        $.each(event.changedTouches, (index, touch) => {
            if (touch.target.movingBox) {
                // Change state to "not-moving-anything" by clearing out
                if (touch.pageX < trashBoundX && touch.pageY < trashBoundY) {
                    touch.target.remove();
                }
                touch.target.movingBox = null;
            } else if (boxesInProgress[touch.identifier]) {
                $(boxesInProgress[touch.identifier].drawingBox).removeClass("box-highlight box-highlight-draw box-highlight-trash box-highlight-zoom")
                boxesInProgress[touch.identifier].drawingBox
                    .bind("touchend", unhighlightBox)
                    .bind("touchstart", startMove);
                delete boxesInProgress[touch.identifier];
            } else if (touch.target.zoomingBox) {
                touch.target.zoomingBox
                    .bind("touchend", unhighlightBox);
            }
        });
    };

    /**
     * Indicates that an element is unhighlightBoxed.
     */
    let unhighlightBox = (event) => $(event.currentTarget).removeClass("box-highlight box-highlight-draw box-highlight-trash box-highlight-zoom");

    let unhighlightTrash = (event) => $(event.currentTarget).removeClass("trash-highlight");

    /**
     * Begins a box move sequence.
     */

    let setupDrawState = () => $(".drawing-area .box").unbind("touchstart").unbind("touchend");
    
    let startDraw = function (event) {
        // NEED TO SAVE EACH TOUCH AS INDEPNDENT OF START DRAW
        // FUNCTION TO KEEP TRACK AND PASS IN THE TOUCH DATA FOR A FINGER FOR A BOX
        $.each(event.changedTouches, (index, touch) => {
            if (!touch.target.movingBox) {
                let boxState = {
                    anchorX: touch.pageX,
                    anchorY: touch.pageY,
                    drawingBox: $("<div></div>")
                        .appendTo(touch.target)
                        .addClass("box")
                        .addClass("box-highlight-draw")
                        .offset({
                            left: touch.pageX,
                            top: touch.pageY
                        })
                        .data({
                            position: { left: touch.pageX, top: touch.pageY },
                            velocity: { x: 0, y: 0, z: 0 },
                            acceleration: { x: 0, y: 0, z: 0 }
                        })
                };
                boxesInProgress[touch.identifier] = boxState;
            }
        })

        event.stopPropagation();
    };

    let startMove = function (event) {
        $.each(event.changedTouches, (index, touch) => {
            // Highlight the element.
            $(touch.target).addClass("box-highlight");
            

            // Take note of the box's current (global) location. Also, set its velocity and acceleration to
            // nothing because, well, _finger_.
            let jThis = $(touch.target);
            let startOffset = jThis.offset();
            jThis
                .addClass("box")
                .data({
                position: startOffset,
                velocity: { x: 0, y: 0, z: 0 },
                acceleration: { x: 0, y: 0, z: 0 }
            });

            var width = jThis.width();
            var height = jThis.height();

            // Set the drawing area's state to indicate that it is
            // in the middle of a move.

            if (touch.pageX < (jThis.data().position.left + width) && 
                touch.pageX > (jThis.data().position.left + width - 25.0) &&
                touch.pageY < (jThis.data().position.top + height) &&
                touch.pageY > (jThis.data().position.top + height - 25.0)) {
                    jThis.removeClass("box-highlight");
                    jThis.addClass("box-highlight-zoom");
                    jThis.anchorX = jThis.data().position.left;
                    jThis.anchorY = jThis.data().position.top;
                    
                    touch.target.zoomingBox = jThis;
            } else {
                touch.target.movingBox = jThis;
            }
            touch.target.deltaX = touch.pageX - startOffset.left;
            touch.target.deltaY = touch.pageY - startOffset.top;
        });

        // Eat up the event so that the drawing area does not
        // deal with it.
        event.stopPropagation();
    };

    /**
     * The motion update routine.
     */
    const FRICTION_FACTOR = 0.99;
    const ACCELERATION_COEFFICIENT = 0.05;
    const FRAME_RATE = 120;
    const FRAME_DURATION = 1000 / FRAME_RATE;

    let lastTimestamp = 0;
    let updateBoxes = (timestamp) => {
        if (!lastTimestamp) {
            lastTimestamp = timestamp;
        }

        // Keep that frame rate under control.
        if (timestamp - lastTimestamp < FRAME_DURATION) {
            window.requestAnimationFrame(updateBoxes);
            return;
        }

        $("div.box").each((index, element) => {
            let $element = $(element);

            // If it's highlighted, we don't accelerate it because it is under a finger.
            if ($element.hasClass("box-highlight") || $element.hasClass("box-highlight-draw") || $element.hasClass("box-highlight-zoom") || $element.hasClass("box-highlight-trash")) {
                return;
            }

            let s = $element.data('position');
            let v = $element.data('velocity');
            let a = $element.data('acceleration');

            // The standard update-bounce sequence.
            s.left += v.x;
            s.top -= v.y;

            v.x += (a.x * ACCELERATION_COEFFICIENT);
            v.y += (a.y * ACCELERATION_COEFFICIENT);
            v.z += (a.z * ACCELERATION_COEFFICIENT);

            v.x *= FRICTION_FACTOR;
            v.y *= FRICTION_FACTOR;
            v.z *= FRICTION_FACTOR;

            let $parent = $element.parent();
            let bounds = {
                left: $parent.offset().left,
                top: $parent.offset().top
            };

            bounds.right = bounds.left + $parent.width();
            bounds.bottom = bounds.top + $parent.height();

            if ((s.left <= bounds.left) || (s.left + $element.width() > bounds.right)) {
                s.left = (s.left <= bounds.left) ? bounds.left : bounds.right - $element.width();
                v.x = -v.x;
            }

            if ((s.top <= bounds.top) || (s.top + $element.height() > bounds.bottom)) {
                s.top = (s.top <= bounds.top) ? bounds.top : bounds.bottom - $element.height();
                v.y = -v.y;
            }

            $(element).offset(s);
        });

        lastTimestamp = timestamp;
        window.requestAnimationFrame(updateBoxes);
    };

    /**
     * Sets up the given jQuery collection as the drawing area(s).
     */
    let setDrawingArea = (jQueryElements) => {
        // Set up any pre-existing box elements for touch behavior.
        jQueryElements
            .addClass("drawing-area")

            // Event handler setup must be low-level because jQuery
            // doesn't relay touch-specific event properties.
            .each((index, element) => {
                element.addEventListener("touchmove", trackDrag, false);
                element.addEventListener("touchend", endDrag, false);
                element.addEventListener("touchstart", startDraw, false);
            })

            .find("div.box").each((index, element) => {
                element.addEventListener("touchstart", startMove, false);
                element.addEventListener("touchend", unhighlightBox, false);

                $(element).data({
                    position: $(element).offset(),
                    velocity: { x: 0, y: 0, z: 0 },
                    acceleration: { x: 0, y: 0, z: 0 }
                });
            });

        // In this sample, device acceleration is the _sole_ determiner of a box's acceleration.
        window.ondevicemotion = (event) => {
            let a = event.accelerationIncludingGravity;
            $("div.box").each((index, element) => {
                $(element).data('acceleration', a);
            });
        };

        // Start the animation sequence.
        window.requestAnimationFrame(updateBoxes);
    };

    // No arrow function here because we don't want lexical scoping.
    $.fn.boxesWithPhysics = function () {
        setDrawingArea(this);
        return this;
    };
})(jQuery);
