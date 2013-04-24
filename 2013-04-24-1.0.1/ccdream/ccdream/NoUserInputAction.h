//
//  NoUserInputAction.h
//  ccdream
//
//  Created by wei zhang on 12-6-11.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "IState.h"

@interface NoUserInputAction : IState {
    
}

-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event;

@end
