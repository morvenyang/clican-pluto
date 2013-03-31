//
//  ConfigViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-31.
//
//

#import "ConfigViewController.h"
#import "Constants.h"
#import "AppDelegate.h"

@implementation ConfigViewController

@synthesize serverIPField = _serverIPField;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"设置";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"设置" image:nil tag:3] autorelease];
        self.serverIPField = [[[UITextField alloc] init] autorelease];
        self.serverIPField.font = [UIFont fontWithName:@"Microsoft YaHei" size:14];
        self.serverIPField.placeholder = @"服务器IP";
        
        self.serverIPField.autocorrectionType = UITextAutocorrectionTypeNo;
        self.serverIPField.keyboardType = UIKeyboardTypeDefault;
        self.serverIPField.returnKeyType = UIReturnKeyDone;
        
        self.serverIPField.clearButtonMode = UITextFieldViewModeAlways;
        self.serverIPField.delegate = self;
        [self.serverIPField setAccessibilityLabel:@"服务器IP"];
    }
    return self;
}

-(void) dealloc{
    TT_RELEASE_SAFELY(_serverIPField);
    [super dealloc];
}

- (void)loadView
{
    [super loadView];
}

#pragma mark -
#pragma mark TTModelViewController
- (void)createModel {
    NSMutableArray* items = [NSMutableArray array];
    
    
    TTTableControlItem* serverIPItem = [TTTableControlItem itemWithCaption:@"服务器IP" control:self.serverIPField];
    [items addObject:serverIPItem];
    TTListDataSource* ds = [[TTListDataSource alloc] initWithItems:items];
    self.dataSource = ds;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    if (textField==self.serverIPField) {
        if(textField.text!=nil&&textField.text.length>0&&![textField.text isEqualToString:@"服务器IP"]){
            [defaults setValue:textField.text forKey:ATV_SERVER_IP_NAME];
            AppDele.serverIP = textField.text;
        }
    }
}
- (void)viewDidLoad
{
    [super viewDidLoad];
	NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    self.serverIPField.text = [defaults stringForKey: ATV_SERVER_IP_NAME];
    if(self.serverIPField.text==nil||self.serverIPField.text.length==0){
        self.serverIPField.text = ATV_SERVER_DEFAULT_IP;
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
