//#pragma once

#include "ExtendedEnum.h"

ENUM(e_direction, none = 1, up = 0, right = 2, down = 4, left = 6)

ENUM(e_entity, empty = 0, wall, iron_wall, base,enemy, hero, bullet)

ENUM(e_collision, static_entity = 0, dynamic_entity)
