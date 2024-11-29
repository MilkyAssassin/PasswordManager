<script lang="ts">
    import { getContext } from "svelte";
    import type { Context } from "../../../types/types";

    
    type sortType = "AlphabeticalOrder"
    | "AlphabeticalOrderReverse"
    | "DateCreated"
            | "DateCreatedReverse";
    type props = {
        search: string;
        sort: sortType
    };
    
    let { search = $bindable(), sort = $bindable() }: props = $props();
    let context:Context = getContext("context");

</script>


<div>
    Search: {search}
    {#if context.isLoading}
        Loading...
    {/if}
</div>