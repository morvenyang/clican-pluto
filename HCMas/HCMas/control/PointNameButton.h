//
//  PointNameButton.h
//  HCMas
//
//  Created by zhang wei on 14-9-10.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PointNameButton : UIButton{
    NSString* _pointName;
}
@property (nonatomic, copy) NSString* pointName;
@end
