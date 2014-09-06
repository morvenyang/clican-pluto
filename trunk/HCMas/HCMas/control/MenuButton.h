//
//  MenuButton.h
//  HCMas
//
//  Created by zhang wei on 14-9-6.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MenuButton : UIButton{
    NSString* _type;
}
@property (nonatomic, copy) NSString* type;
@end
