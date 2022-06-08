(: Your XQuery goes here :)

<dramaticActors>
    {
        for $actor in doc("/Users/bernhardcerncic/Documents/Uni/SEMI/UE2/template-2/resources/screening-plan-xsd.xml")/screening-plan/actors/actor[

        (for $o in distinct-values(
                for $x in doc("/Users/bernhardcerncic/Documents/Uni/SEMI/UE2/template-2/resources/screening-plan-xsd.xml")/screening-plan/movies/movie/roles/*/@actor_ref
                let $z := doc("/Users/bernhardcerncic/Documents/Uni/SEMI/UE2/template-2/resources/screening-plan-xsd.xml")/screening-plan/movies/movie[roles/*/@actor_ref = $x]
                where count($z) > 2
                return $x) return $o) = @aid]
        let $p := count(doc("/Users/bernhardcerncic/Documents/Uni/SEMI/UE2/template-2/resources/screening-plan-xsd.xml")/screening-plan/movies/movie[roles/*/@actor_ref = $actor/@aid])
        let $e := doc("/Users/bernhardcerncic/Documents/Uni/SEMI/UE2/template-2/resources/screening-plan-xsd.xml")/screening-plan/movies/movie[roles/*/@actor_ref = $actor/@aid]/roles/*
        let $i := for $l in doc("/Users/bernhardcerncic/Documents/Uni/SEMI/UE2/template-2/resources/screening-plan-xsd.xml")/screening-plan/actors/actor[@aid = $e/@actor_ref] return $l
        return <actor born-in="{$actor/@birth_country}" name="{$actor/first-name} {$actor/last-name}">

            <movieCount>{$p}</movieCount>{for $p in $i return <movie co-actors="{count(for $s in $i return $s) - 1}">{$p/name}</movie>}

        </actor>


    }
</dramaticActors>