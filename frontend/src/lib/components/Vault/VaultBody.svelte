<script lang="ts">
    import { getContext } from "svelte";
    import type { Context, Entry } from "../../../types/types";
    import { fade, fly } from "svelte/transition";

    type sortType =
        | "AlphabeticalOrder"
        | "AlphabeticalOrderReverse"
        | "DateCreated"
        | "DateCreatedReverse";
    type props = {
        search: string;
        sort: sortType;
    };

    let { search = $bindable(), sort = $bindable() }: props = $props();
    let context: Context = getContext("context");
    let vault = $derived((context.user?.vault || []).filter(filter));
    function filter(e: Entry) {
        if (e.title.includes(search)) return true;
        if (e.notes.includes(search)) return true;
        if (e.url.includes(search)) return true;
        if (e.username.includes(search)) return true;
        return false;
    }
    let selectedEntry: Entry | undefined | null = $state(undefined);
    $inspect(selectedEntry);
</script>

<div>
    {#if context.isLoading}
        Loading...
    {:else if 0 < vault.length}
        {#each vault as entry}
            <button
                class="flex justify-between px-10 py-4 cursor-pointer w-full bg-[#fef7ff] hover:bg-white"
                onclick={() => (selectedEntry = entry)}
            >
                <div class="flex gap-4">
                    <div
                        class="rounded-full bg-[#d8cbea] text-[#625282] h-7 w-7 text-center align-middle leading-7"
                    >
                        {entry.username[0]}
                    </div>
                    {entry.title}
                </div>
                <span>|</span>
            </button>
        {/each}
    {:else}
        Your vault is empty, please add a password
    {/if}

    {#if selectedEntry === undefined || selectedEntry !== null}
        <div
            class="fixed h-3/4 bottom-0 left-28 right-28 bg-blue-900 z-50"
            transition:fly={{ y: 200, duration: 1000 }}
        >
            Selected entry: {vault[0]}
        </div>

        <button
            class="fixed left-0 right-0 bottom-0 top-0 bg-black opacity-80"
            aria-label="Close opened modal"
            onclick={() => (selectedEntry = null)}
            transition:fade={{ duration: 1000 }}
        >
        </button>
    {/if}
</div>
