<script lang="ts">
    import { getContext } from "svelte";
    import type { Context, Entry } from "@my-types/types";
    import { fade, fly } from "svelte/transition";
    import SoloEntry from "../SoloEntry.svelte";

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
    let vault = $derived(
        (context.user?.vault || []).filter(filter).sort(compareFn)
    );
    function filter(e: Entry) {
        if (e.title.includes(search)) return true;
        if (e.notes.includes(search)) return true;
        if (e.url.includes(search)) return true;
        if (e.username.includes(search)) return true;
        return false;
    }

    function compareFn(e1: Entry, e2: Entry): number {
        switch (sort) {
            case "AlphabeticalOrder":
                return e1.title.localeCompare(e2.title);
            case "AlphabeticalOrderReverse":
                return e2.title.localeCompare(e1.title);
            case "DateCreated":
                return e1.dateCreated.getTime() - e2.dateCreated.getTime();
            case "DateCreatedReverse":
                return e2.dateCreated.getTime() - e1.dateCreated.getTime();
        }
    }

    let selectedEntry: Entry | undefined | null = $state(null);
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

    {#if selectedEntry !== null}
        <SoloEntry
            bind:selectedEntry
            close={() => {
                if (
                    window.confirm(
                        "Are you sure you want to close without saving your entry?"
                    )
                )
                    selectedEntry = null;
            }}
            save={() => alert("Write save func")}
        />
    {/if}
</div>
