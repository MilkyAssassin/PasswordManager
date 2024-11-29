<script lang="ts">
    import { fly, fade } from "svelte/transition";
    import type { Entry } from "@my-types/types";
    type props = {
        entry: Entry;
    };
    let { selectedEntry = $bindable() } = $props();
    let entry = $state($state.snapshot(selectedEntry));
</script>

<div
    class="fixed h-3/4 bottom-0 left-28 right-28 bg-[#625282] z-50 p-5 rounded-t-2xl"
    transition:fly={{ y: 200, duration: 1000 }}
>
    <h1 class="text-xl text-center text-white">Edit Entry</h1>
    <div class="grid grid-cols-2 gap-8">
        <div>
            <label class="block" for="">Title:</label>
            <input class='w-full' type="text" bind:value={entry.title} />
        </div>
        <div>
            <label class="block" for="">Username:</label>
            <input class='w-full' type="text" bind:value={entry.username} />
        </div>
        <div>
            <label class="block" for="">Password:</label>
            <input class='w-full' type="password" bind:value={entry.password} />
        </div>
        <div>
            <label class="block" for="">URL:</label>
            <input class='w-full' type="text" bind:value={entry.url} />
        </div>
        <div>
            <label class="block" for="">Date created:</label>
            <input class='w-full' type="text" disabled value={entry.dateCreated} />
        </div>
        <div>
            <label class="block" for="">Last used:</label>
            <input class='w-full' type="text" disabled value={entry.lastUsed} />
        </div>
    </div>
    <div class="w-full">
        <label class="block" for="">Notes:</label>
        <textarea class="block w-full h-40" bind:value={entry.notes}> </textarea>
    </div>
</div>

<button
    class="fixed left-0 right-0 bottom-0 top-0 bg-black opacity-80"
    aria-label="Close opened modal"
    onclick={() => {
        if (
            window.confirm(
                "Are you sure you want to close without saving your entry?"
            )
        )
            selectedEntry = null;
    }}
    transition:fade={{ duration: 1000 }}
>
</button>
