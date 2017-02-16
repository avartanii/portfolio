$(() => {
    let $log = $(".event-log");
    let logEvent = (message) => {
        $log.text($log.text() + message + "\n")
            .scrollTop($log[0].scrollHeight - $log.height());
    };

    $(".slide-this").slider({
        change: function (oldPosition, newPosition, max) {
            logEvent("Slider: Slid from " + oldPosition + " to " + newPosition + " with max " + max);
            let slider = $("#slider");
            let textValue = $("#par");
            var text = "Slider Value: " + slider.data('slider-value').toString();
            textValue.text(text);
        }
    });
});