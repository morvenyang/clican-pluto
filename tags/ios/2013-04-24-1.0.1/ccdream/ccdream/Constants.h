//
//  Constants.h
//  ccdream
//
//  Created by wei zhang on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Weapon.h"

#define MAP_POINT_SIZE_X 48
#define MAP_POINT_SIZE_Y 40
@interface Constants : NSObject {
    
}

+(Weapon*) getWeapon:(NSString*) code;
@end
