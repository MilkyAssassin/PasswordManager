<script lang="ts">
    import { getContext } from "svelte";
    import type { Context } from "../../../types/types";

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
    let vault = $derived(context.user?.vault||[]);
</script>

<div>
    {#if context.isLoading}
        Loading...
    {:else if 0 < vault.length}
        {#each vault as entry}
            <div class="flex justify-between px-10 py-4">
                <div class="flex gap-4">
                    <div class="rounded-full bg-[#d8cbea] text-[#625282] h-7 w-7 text-center align-middle leading-7">{entry.username[0]}</div>
                    {entry.title}
                </div>
                <span>|</span>
            </div>
        {/each}
    {:else}
        Your vault is empty, please add a password
    {/if}
</div>
