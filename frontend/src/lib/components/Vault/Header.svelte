<script lang="ts">
    type sortType = "AlphabeticalOrder"
            | "AlphabeticalOrderReverse"
            | "DateCreated"
            | "DateCreatedReverse";
    type props = {
        search: string;
        sort: sortType
    };

    let { search = $bindable(), sort = $bindable() }: props = $props();
    let sort_options:Array<sortType> = [
        "AlphabeticalOrder",
        "AlphabeticalOrderReverse",
        "DateCreated",
        "DateCreatedReverse",
    ];
    function getRandomChoice(arr: Array<sortType>) {
        const randomIndex = Math.floor(Math.random() * arr.length);
        return arr[randomIndex];
    }

</script>

<div class="w-full h-16 bg-green-900">
    <div class="h-4/6 relative top-1/2 -translate-y-1/2 ml-5 flex">
        <div class="rounded-full bg-white px-4 h-full w-96">
            <span>/O</span>
            <input
                bind:value={search}
                type="text"
                name="search"
                placeholder="Search vault"
                id=""
                class="bg-green-400 w-80 h-full"
            />
        </div>
        <button
            class=""
            onclick={() => {
                alert("Fix sort");
                sort = getRandomChoice(sort_options);
            }}
        >
            = Sort {sort}
        </button>
    </div>
</div>