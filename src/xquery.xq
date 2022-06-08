(: Your XQuery goes here :)


<dramaticActors>{

for $r in doc("../resources/screening-plan-xsd.xml")/screening-plan/actors/actor[

    (for $o in distinct-values(
for $x in doc("../resources/screening-plan-xsd.xml")/screening-plan/movies/movie/roles/*/@actor_ref
let $z := doc("../resources/screening-plan-xsd.xml")/screening-plan/movies/movie[roles/*/@actor_ref = $x]
where count($z) > 2
return $x) return $o) = @aid]
let $p := count(doc("../resources/screening-plan-xsd.xml")/screening-plan/movies/movie[roles/*/@actor_ref = $r/@aid])
let $e := doc("../resources/screening-plan-xsd.xml")/screening-plan/movies/movie[roles/*/@actor_ref = $r/@aid]/roles/*
let $i := for $l in doc("../resources/screening-plan-xsd.xml")/screening-plan/actors/actor[@aid = $e/@actor_ref] return $l
 return <actor born-in="{$r/@birth_country}" name="{$r/first-name} {$r/last-name}">
 <movieCount>{$p}</movieCount>{for $p in $i return <movie co-actors="{count(for $s in $i return $s) - 1}">{$p/first-name}{$p/last-name}</movie>}
 </actor>
}



</dramaticActors>