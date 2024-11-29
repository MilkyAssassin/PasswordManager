<script lang="ts">
    import { fly, fade } from "svelte/transition";
    import type { Entry } from "@my-types/types";
    type props = {
        selectedEntry: Entry;
    }
    let {selectedEntry = $bindable()} = $props()

</script>
<div
    class="fixed h-3/4 bottom-0 left-28 right-28 bg-[#625282] z-50 p-5"
    transition:fly={{ y: 200, duration: 1000 }}
>
    <h1 class="text-xl text-center text-white">Edit Entry</h1>
    Selected entry: {selectedEntry}
</div>

<button
    class="fixed left-0 right-0 bottom-0 top-0 bg-black opacity-80"
    aria-label="Close opened modal"
    onclick={() => (selectedEntry = null)}
    transition:fade={{ duration: 1000 }}
>
</button>
