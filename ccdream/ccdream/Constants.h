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

#define MAP_POINT_SIZE 32
@interface Constants : NSObject {
    
}

+(Weapon*) getWeapon:(NSString*) code;
@end
